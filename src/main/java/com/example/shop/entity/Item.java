package com.example.shop.entity;

import com.example.shop.constant.ItemSellStatus;
import com.example.shop.dto.ItemFormDto;
import com.example.shop.exception.OutOfStockException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "item")
@Getter
@ToString
@Setter
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="item_id")
    private Long id; //상품코드

    @Column(nullable = false, length = 50)
    private String itemNm; //상품명

    @Column(nullable = false, name="price")
    private int price; //가격

    @Column(nullable = false)
    private int stockNumber;    //재고수량

    @Lob
    @Column(nullable = false)
    private String itemDetail;  //상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; //상품 판매 상태

    public void updateItem(ItemFormDto itemFormDto) {
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    public void removeStock(int stockNumber) {
        int restStock = this.stockNumber - stockNumber;

        if (restStock < 0) {
            throw new OutOfStockException("상품의 재고가 부족합니다.(현재 재고 수량:" +
                    this.stockNumber + ")");
        }
        this.stockNumber = restStock;
    }
    //상품의 재고를 증가시키는 메서드
    public void addStock(int stockNumber) {
        this.stockNumber += stockNumber;
    }
}
