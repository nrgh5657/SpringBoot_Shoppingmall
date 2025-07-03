package com.example.shop.dto;

import com.example.shop.constant.ItemSellStatus;
import com.example.shop.entity.Item;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter@Setter

public class ItemFormDto {


    private Long id; //상품코드

    @NotBlank(message="상품명은 필수 입력 값입니다.")
    private String itemNm; //상품명

    @NotNull(message="가격은 필수 입력 값입니다")
    private Integer price; //가격

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stockNumber;    //재고수량

    @NotBlank(message = "상세설명은 필수 입력 값입니다.")
    private String itemDetail;  //상품 상세 설명

    private ItemSellStatus itemSellStatus; //상품 판매 상태

    //상품 이미지를 리스트로 저장
    private List<ItemImgDto> itemImgDtoList= new ArrayList<>();
    /* ItemImg entity에 item_img_id값을 리크스로 가지고 있다
       이미지 수정시 특정 삼품에서 전체 이미지를 수정하지 않고,
       개별 이미지를 수정하기 위한 용도로 item_img_id값을 가지고 있지 위해서
    */
    private List<Long> itemImgIds = new ArrayList<>();
    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem(){
        return modelMapper.map(this, Item.class);
    }
    
    //Item -> ItemFormDto로 변환
    public static ItemFormDto of(Item item) {
        return modelMapper.map(item, ItemFormDto.class);
    }
}
