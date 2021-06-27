package com.dh.sp.web.mvc.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Data
@Builder
@ToString
@RequiredArgsConstructor
public class OrderDto {
    private final UUID id;
    private final UUID customerId;
    private final String creditCard;
    private final String creditCartType;
}
