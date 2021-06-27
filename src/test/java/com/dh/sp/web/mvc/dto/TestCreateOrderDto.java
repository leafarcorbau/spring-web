package com.dh.sp.web.mvc.dto;

import com.dh.sp.web.mvc.util.TestUtil;

import java.util.UUID;

public class TestCreateOrderDto{

    public static CreateOrderDto.CreateOrderDtoBuilder getInstance(final UUID seed){
        return CreateOrderDto.builder()
                .creditCard(TestUtil.genField("cc", seed));
    }
}
