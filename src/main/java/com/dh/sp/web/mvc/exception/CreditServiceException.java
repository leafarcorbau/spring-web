package com.dh.sp.web.mvc.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class CreditServiceException extends RuntimeException{
    private  UUID userId;
    public CreditServiceException(final String msg, final UUID userId, final Exception e){
        super(msg, e);
        this.userId = userId;
    }
}
