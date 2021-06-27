package com.dh.sp.web.mvc.service;

import com.dh.sp.web.mvc.dto.CreditCardInfoDto;
import com.dh.sp.web.mvc.exception.CreditServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreditCardService {

    public static final String CREDIT_CLIENT_ERROR = "Credit Client Error";
    @Value("${dh.credit.service.url}")
    private String creditServiceUrl;
    private final RestTemplate restTemplate;

    public Optional<CreditCardInfoDto> getCreditCardInfo(final UUID userId) {
        try{
            final URI uri = URI.create(format(creditServiceUrl+"/user/%s", userId));
            final ResponseEntity<CreditCardInfoDto> responseEntity = restTemplate.getForEntity(uri, CreditCardInfoDto.class);
            final CreditCardInfoDto creditCardInfoDto = responseEntity.getBody();
            return Optional.of(creditCardInfoDto);
        }catch (HttpClientErrorException e){
            log.error("process= getCreditUserDto, userId= {}, status: error, code= {}", userId, e.getStatusCode(), e);
            return Optional.empty();
        }catch (RuntimeException e){
            throw new CreditServiceException(CREDIT_CLIENT_ERROR, userId, e);
        }
    }
}
