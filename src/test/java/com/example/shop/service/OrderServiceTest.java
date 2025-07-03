package com.example.shop.service;

import com.example.shop.constant.ItemSellStatus;
import com.example.shop.constant.OrderStatus;
import com.example.shop.dto.OrderDto;
import com.example.shop.dto.OrderHistDto;
import com.example.shop.entity.Item;
import com.example.shop.entity.Member;
import com.example.shop.entity.Order;
import com.example.shop.entity.OrderItem;
import com.example.shop.repository.ItemRepository;
import com.example.shop.repository.MemberRepository;
import com.example.shop.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@Slf4j
@WithMockUser(username = "admin@amdin.com", roles ="ADMIN")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Test
    @DisplayName("DB에서 주문테스트 ㄱㄱ")
    @Transactional
    public void orderTest(){
        String email = "admin@amdin.com";
        OrderDto orderDto = new OrderDto();
        orderDto.setCount(2);
        orderDto.setItemId(14L);

        Long order = orderService.order(orderDto, email);
        log.info("=====order==== : {}", order);

        Order savedOrder = orderRepository.findById(order)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        log.info("--------savedOrder-------------: {}", savedOrder);

        savedOrder.getOrderItems().forEach(orderItem -> {log.info("OrderItem : {}", orderItem);});

    }
    @Transactional
    @Test
    public void getOrderListTest(){
       String email ="admin@admin.com";
        Pageable pageable = PageRequest.of(0, 5);
        Page<OrderHistDto> orderHistDtoList = orderService.getOrderList(email, pageable);
        orderHistDtoList.forEach(list -> log.info("OrderHistDto : {}", list));
        log.info("totalCount : {}", orderHistDtoList.getTotalElements());
    }

    @Test
    public void cancelOrderTest() {
        // DB에 있는 회원과 상품 사용
        String email = "admin@admin.com";
        Long itemId = 18L;
        int orderCount = 2;

        // 주문 전 상품 재고 저장
        Item itemBefore = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException());
        int stockBefore = itemBefore.getStockNumber();

        // 주문 생성
        OrderDto orderDto = new OrderDto();
        orderDto.setItemId(itemId);
        orderDto.setCount(orderCount);

        Long orderId = orderService.order(orderDto, email);

        // 주문 취소
        orderService.cancelOrder(orderId);

        // 주문 상태 확인
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException());

        // 재고 다시 조회
        Item itemAfter = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException());

        // 검증: 주문 상태와 재고 복원 여부
        assertEquals(OrderStatus.CANCEL, order.getOrderStatus());
        assertEquals(stockBefore, itemAfter.getStockNumber());
    }








}