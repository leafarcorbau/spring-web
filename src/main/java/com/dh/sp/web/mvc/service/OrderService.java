package com.dh.sp.web.mvc.service;

import com.dh.sp.web.mvc.dto.CreateOrderDto;
import com.dh.sp.web.mvc.dto.CreditCardInfoDto;
import com.dh.sp.web.mvc.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final CreditCardService creditCardService;

    public OrderDto createOrder(final CreateOrderDto createOrderDto, final UUID userId) {

        OrderDto.OrderDtoBuilder orderDtoBuilder = OrderDto.builder()
                .id(UUID.randomUUID())
                .creditCard(createOrderDto.getCreditCard())
                .customerId(userId);

        final Optional<CreditCardInfoDto> optionalCreditCardInfoDto = creditCardService.getCreditCardInfo(userId);

        if(optionalCreditCardInfoDto.isPresent())
            orderDtoBuilder.creditCartType(optionalCreditCardInfoDto.get().getCreditCartType());

        return orderDtoBuilder.build();
    }

    public OrderDto findById(final UUID orderId, final UUID userId) {
        return OrderDto.builder()
                .id(orderId)
                .creditCard(format("cc-%s",orderId.toString().substring(orderId.toString().length() - 4)))
                .customerId(userId)
                .build();
    }
}
