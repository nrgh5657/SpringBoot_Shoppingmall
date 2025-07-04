package com.example.shop.dto;


import com.example.shop.constant.OrderStatus;
import com.example.shop.entity.Order;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter@Setter
@ToString
public class OrderHistDto {
    private Long orderId;
    private String orderDate;
    private OrderStatus orderStatus;
    private List<OrderItemDto> orderItemDtoList = new ArrayList<>();

    public OrderHistDto(Order order) {
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate().
                format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.orderStatus = order.getOrderStatus();
    }

    public void addOrderItem(OrderItemDto orderItemDto) {
       orderItemDtoList.add(orderItemDto);
    }
}
