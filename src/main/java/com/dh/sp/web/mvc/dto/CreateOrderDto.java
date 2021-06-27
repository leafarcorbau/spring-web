package com.dh.sp.web.mvc.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@Builder
@ToString
@RequiredArgsConstructor
public class CreateOrderDto implements Serializable {
    private final String creditCard;
}
