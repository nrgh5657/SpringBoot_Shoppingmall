package com.example.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberFormDto {
    private String name;
    private String email;
    private String password;
    private String address;
}
