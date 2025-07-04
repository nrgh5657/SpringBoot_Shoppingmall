package com.example.shop.controller;

import com.example.shop.dto.ItemSearchDto;
import com.example.shop.dto.MainItemDto;
import com.example.shop.service.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class MainController {

    private final ItemService itemService;

    public MainController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/")
    public String main(ItemSearchDto itemSearchDto,
                       Optional<Integer> page,
                       Model model) {
        Pageable pageable = PageRequest.of(
            page.isPresent()? page.get() : 0,6
        );
        Page<MainItemDto> items =  itemService.getMainItemPage(itemSearchDto, pageable);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);
        return "main";
    }

    @GetMapping("/camera")
    public String showCameraPage() {
        return "cameraTest";  // templates/camera/cameraView.html
    }
}
