package com.dh.sp.web.mvc.controller;

import com.dh.sp.web.mvc.IntegrationTest;
import com.dh.sp.web.mvc.dto.*;
import com.dh.sp.web.mvc.service.CreditCardService;
import com.dh.sp.web.mvc.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.HttpError;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.UUID;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class OrderControllerIntegrationTest extends IntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MockServerClient mockServerClient;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldFindOrderById() {
        //Given
        final UUID orderId = UUID.randomUUID();
        final UUID userId = UUID.randomUUID();
        final String url = format(TestUtil.getBaseUrl(port)+"/order/%s", orderId);
        final OrderDto expected = TestOrderDto.getInstance(orderId)
                .customerId(userId)
                .build();

        final HttpHeaders headers = new HttpHeaders();
        headers.set("userId", String.valueOf(userId));
        final HttpEntity entity = new HttpEntity(headers);

        //When
        final ResponseEntity<OrderDto> response = this.restTemplate.exchange(url, HttpMethod.GET, entity, OrderDto.class);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        final OrderDto orderDto = response.getBody();
        assertThat(orderDto).isEqualTo(expected);
    }

    @Test
    void shouldCreateOrder() {
        //Given
        final UUID orderId = UUID.randomUUID();
        final UUID userId = UUID.randomUUID();
        final String url = TestUtil.getBaseUrl(port)+"/order";
        final CreateOrderDto createOrderDto = TestCreateOrderDto.getInstance(orderId).build();
        final OrderDto expected = TestOrderDto.getInstance(orderId)
                .customerId(userId)
                .creditCartType("VISA")
                .build();

        final HttpHeaders headers = new HttpHeaders();
        headers.set("userId", String.valueOf(userId));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        final HttpEntity<CreateOrderDto> entity = new HttpEntity(createOrderDto, headers);

        final HttpRequest request = request()
                .withMethod(HttpMethod.GET.name())
                .withPath(format("/user/%s", userId));
        final HttpResponse httpResponse = response()
                .withStatusCode(HttpStatus.OK.value())
                .withBody("{\"creditCartType\":\"VISA\"}");

        mockServerClient.when(request)
                .respond(httpResponse);

        //When
        final ResponseEntity<OrderDto> response = this.restTemplate.postForEntity(url, entity, OrderDto.class);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        final OrderDto orderDto = response.getBody();
        assertThat(orderDto).isEqualToIgnoringGivenFields(expected,"id");

        mockServerClient.verify(request);
    }

    @Test
    void shouldCreateOrderWithNotFoundCreditUser() {
        //Given
        final UUID orderId = UUID.randomUUID();
        final UUID userId = UUID.randomUUID();
        final String url = TestUtil.getBaseUrl(port)+"/order";
        final CreateOrderDto createOrderDto = TestCreateOrderDto.getInstance(orderId).build();
        final OrderDto expected = TestOrderDto.getInstance(orderId)
                .customerId(userId)
                .build();

        final HttpHeaders headers = new HttpHeaders();
        headers.set("userId", String.valueOf(userId));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        final HttpEntity<CreateOrderDto> entity = new HttpEntity(createOrderDto, headers);

        final HttpRequest request = request()
                .withMethod(HttpMethod.GET.name())
                .withPath(format("/user/%s", userId));
        final HttpResponse httpResponse = response()
                .withStatusCode(HttpStatus.NOT_FOUND.value());

        mockServerClient.when(request)
                .respond(httpResponse);

        //When
        final ResponseEntity<OrderDto> response = this.restTemplate.postForEntity(url, entity, OrderDto.class);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        final OrderDto orderDto = response.getBody();
        assertThat(orderDto).isEqualToIgnoringGivenFields(expected,"id");

        mockServerClient.verify(request);
    }

    @Test
    void shouldCreateOrderWithRuntimeError() {
        //Given
        final UUID orderId = UUID.randomUUID();
        final UUID userId = UUID.randomUUID();
        final String url = TestUtil.getBaseUrl(port)+"/order";
        final CreateOrderDto createOrderDto = TestCreateOrderDto.getInstance(orderId).build();
        final ErrorResponseDto expected = ErrorResponseDto.builder()
                .userId(userId)
                .msg(CreditCardService.CREDIT_CLIENT_ERROR)
                .build();

        final HttpHeaders headers = new HttpHeaders();
        headers.set("userId", String.valueOf(userId));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        final HttpEntity<CreateOrderDto> entity = new HttpEntity(createOrderDto, headers);

        final HttpRequest request = request()
                .withMethod(HttpMethod.GET.name())
                .withPath(format("/user/%s", userId));

        mockServerClient.when(request)
                .error(HttpError.error().withDropConnection(true));

        //When
        final ResponseEntity<ErrorResponseDto> response = this.restTemplate.postForEntity(url, entity, ErrorResponseDto.class);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        final ErrorResponseDto errorResponseDto = response.getBody();
        assertThat(errorResponseDto).isEqualTo(expected);
        mockServerClient.verify(request);
    }
}
