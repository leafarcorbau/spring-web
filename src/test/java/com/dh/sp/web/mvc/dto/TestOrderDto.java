package com.dh.sp.web.mvc.dto;

import com.dh.sp.web.mvc.util.TestUtil;

import java.util.UUID;

public class TestOrderDto{

    public static OrderDto.OrderDtoBuilder getInstance(final UUID seed){
        return OrderDto.builder()
                .id(seed)
                .customerId(seed)
                .creditCard(TestUtil.genField("cc", seed));

    }


}
