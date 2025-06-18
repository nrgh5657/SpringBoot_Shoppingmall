package com.example.shop.controller;

import com.example.shop.dto.ItemDto;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/thymeleaf")
public class ThymeleafController {
    @GetMapping (value = "/ex01")
     public String ThymeLeafExample01(Model model){
        model.addAttribute("data", "타임리프 예제입니다.");
        return  "thymeleafEx/thymeleafEx01";
     }

     @GetMapping ("/ex02")
    public String ThymeLeafExample02(Model model){
         ItemDto itemDto = ItemDto.builder()
                 .itemDetail("상품 상세 설명")
                 .itemNm("테스트 상품1")
                 .price(10000)
                 .regTime(LocalDateTime.now())
                 .build();
         model.addAttribute("itemDto", itemDto);
         return "thymeleafEx/thymeleafEx02";
     }

    @GetMapping ("/ex03")
    public String ThymeLeafExample03(Model model){
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (int i=0; i<=10; i++){
        ItemDto itemDto = ItemDto.builder()
                .itemDetail("상품 상세 설명")
                .itemNm("테스트 상품1")
                .price(10000)
                .regTime(LocalDateTime.now())
                .build();
        itemDtoList.add(itemDto);
        }
        model.addAttribute("itemDtoList", itemDtoList);
        return "thymeleafEx/thymeleafEx03";
    }
    @GetMapping ("/ex04")
    public String ThymeLeafExample04(Model model){
        List<ItemDto> itemDtoList = new ArrayList<>();

        for (int i=0; i<=10; i++){
            ItemDto itemDto = ItemDto.builder()
                    .itemDetail("상품 상세 설명")
                    .itemNm("테스트 상품1")
                    .price(10000)
                    .regTime(LocalDateTime.now())
                    .build();
            itemDtoList.add(itemDto);
        }
        model.addAttribute("itemDtoList", itemDtoList);
        return "thymeleafEx/thymeleafEx04";
    }

    @GetMapping("/ex05")
    public String ThymeLeafExample05(){
        return "thymeleafEx/thymeleafEx05";
    }

    @GetMapping("/ex06")
    public String ThymeLeafExample06(@RequestParam("param1") String p1, @RequestParam("param2") String p2, Model model){
        model.addAttribute("param1", p1);
        model.addAttribute("param2", p2);
        return "thymeleafEx/thymeleafEx06";
    }

}
