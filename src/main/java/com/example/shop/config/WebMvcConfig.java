package com.example.shop.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration

public class WebMvcConfig implements WebMvcConfigurer {
    @Value("${uploadPath}")
    String uploadPath;

    @Override
    //http://localhos:8080/images/test.jpg
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //정적 자원의 경로 매핑을 성정하기 위한 메터스 (Spring Mvc에서 오버라이드 하여 사용
        registry.addResourceHandler("/images/**")
                //웹브라우저에서 "/images/파일명으로 접근하면,
               // 실제 파일이 저장된 위치에 있는 파일을 제공하도록 매핑 설정
                .addResourceLocations(uploadPath);

        //실제 파일이 저장된 물리적 경로 또는 UR 셩록
        //예: file:///C//upload/ 또는 classpath:/static/image
    }
}
