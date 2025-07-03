package com.example.shop.repository;

import com.example.shop.entity.Item;
import com.example.shop.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long>, QuerydslPredicateExecutor<Item> {

    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);

    //대표이미지 검색
    ItemImg findByItemIdAndRepimgYn(Long itemId, String repimgYn);
}
