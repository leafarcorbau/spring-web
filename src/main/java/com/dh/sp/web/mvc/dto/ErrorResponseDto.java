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
public class ErrorResponseDto {
    private final UUID userId;
    private final String msg;
}
