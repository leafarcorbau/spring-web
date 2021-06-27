package com.dh.sp.web.mvc.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString
@RequiredArgsConstructor
public class CreditCardInfoDto {
    private final String creditCartType;
}
