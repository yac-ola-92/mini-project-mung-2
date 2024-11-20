package com.example.mung;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /uploads/** 요청에 대해 C:/uploads/ 폴더에서 파일을 찾도록 설정
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:///C:/uploads/");

        // /static/** 경로에 대해서도 /static 폴더 내의 파일들을 서빙
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/static/assets/");

        registry.addResourceHandler("/index.html")
                .addResourceLocations("classpath:/static/dist/");
    }
}
