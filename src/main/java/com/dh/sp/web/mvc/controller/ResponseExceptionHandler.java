package com.dh.sp.web.mvc.controller;

import com.dh.sp.web.mvc.dto.ErrorResponseDto;
import com.dh.sp.web.mvc.exception.CreditServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ResponseExceptionHandler {

    @ExceptionHandler(value={CreditServiceException.class})
    protected ResponseEntity<ErrorResponseDto> handleCreditServiceException(final CreditServiceException e) {

        log.error("process= getCreditUserDto, userId= {}, status: error", e.getUserId(),  e);

        final ErrorResponseDto errorResponseDto = ErrorResponseDto
                .builder()
                .userId(e.getUserId())
                .msg(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDto);
    }
}
