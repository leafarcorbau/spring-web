package com.dh.sp.web.mvc.controller;

import com.dh.sp.web.mvc.dto.CreateOrderDto;
import com.dh.sp.web.mvc.dto.OrderDto;
import com.dh.sp.web.mvc.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody final CreateOrderDto createOrderDto,
                                                @RequestHeader final UUID userId){
        final OrderDto orderDto = orderService.createOrder(createOrderDto, userId);
        return ResponseEntity.ok(orderDto);
    }

    @GetMapping(value = "/{orderId}")
    public ResponseEntity<OrderDto> findOrderById(@PathVariable final UUID orderId,
                                                  @RequestHeader final UUID userId){
        final OrderDto orderDto = orderService.findById(orderId, userId);
        return ResponseEntity.ok(orderDto);
    }


}
