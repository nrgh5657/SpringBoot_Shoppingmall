package com.example.shop.service;

import com.example.shop.dto.CartItemDto;
import com.example.shop.entity.CartItem;
import com.example.shop.repository.CartItemRepository;
import com.example.shop.repository.ItemRepository;
import com.example.shop.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@Slf4j
class CartServiceTest {

    @Autowired
    private CartService cartService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ItemRepository itemRepository;

    String email = "admin@admin.com";

    @Test
    @Transactional
    public void testAddCartItem(){

        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setCount(2);
        cartItemDto.setItemId(8L);
        Long cartItemId = cartService.addCart(cartItemDto, email);

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);

        log.info("CartItem info : {}", cartItem);

        assertEquals(cartItem.getItem().getId(), cartItemDto.getItemId());



    }
}