package com.example.shop.service;

import com.example.shop.dto.OrderDto;
import com.example.shop.dto.OrderHistDto;
import com.example.shop.dto.OrderItemDto;
import com.example.shop.entity.*;
import com.example.shop.repository.ItemImgRepository;
import com.example.shop.repository.ItemRepository;
import com.example.shop.repository.MemberRepository;
import com.example.shop.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final ItemImgRepository itemImgRepository;

    // orderDto(맥주 2병), email(1번테이블)
    public Long order(OrderDto orderDto, String email) {
        //맥주 있는지 확인(상품 정보 확인)
        Item item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(() -> new EntityNotFoundException());
         //email(1번 테이블)
        Member member = memberRepository.findByEmail(email);

        List<OrderItem> orderItemList = new ArrayList<>();

        //주문서 작성(맥주, 2병.....)
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());

        orderItemList.add(orderItem);

        //직원에게 주문서 전달
        Order order = Order.createOrder(member, orderItemList);

        //주방에 메시지 전달....DB저장
        orderRepository.save(order);

        return order.getId();
    }
    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable) {
        // 1. 현재 로그인한 사용자의 주문 목록을 페이징 처리하여 조회
        List<Order> orders = orderRepository.findOrders(email, pageable);

        // 2. 전체 주문 수 (페이징 처리를 위한 total count)
        Long totalCount = orderRepository.countOrders(email);

        // 3. 반환할 주문 이력 DTO 리스트 생성
        List<OrderHistDto> orderHistDtos = new ArrayList<>();

        // 4. 각 주문에 대해 반복
        for (Order order : orders) {
            // 주문 정보를 기반으로 OrderHistDto 생성
            OrderHistDto orderHIstDto = new OrderHistDto(order);
            // 주문에 포함된 상품 목록(OrderItem) 조회
            List<OrderItem> orderItems = order.getOrderItems();
            // 각 주문 상품에 대해 반복
            for (OrderItem orderItem : orderItems) {
                // 해당 주문 상품의 대표 이미지 조회 (repimgYn = "Y")
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepimgYn
                        (orderItem.getItem().getId(),"Y");
                // 주문 상품 + 이미지 URL을 담은 DTO 생성
                OrderItemDto orderItemDTO = new OrderItemDto(orderItem, itemImg.getImgUrl());
                // 생성된 주문 상품 DTO를 OrderHistDto에 추가
                orderHIstDto.addOrderItem(orderItemDTO);
            }
            // 완성된 주문 이력 DTO를 최종 리스트에 추가
            orderHistDtos.add(orderHIstDto);
        }
        // 5. 페이징 정보를 포함한 결과 Page 객체로 반환
        return new PageImpl<>(orderHistDtos, pageable, totalCount);
    }

    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email) {
        Member curMember = memberRepository.findByEmail(email);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException());
        Member savedMember = order.getMember();
        if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) {
            return false;
        }
        return true;
    }

    public void cancelOrder(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException());
        order.cancelOrder();
    }

}
