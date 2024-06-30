package com.example.mdmggreal.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:3000",
                        "https://vsgg.co.kr",
                        "https://www.vsgg.co.kr",
                        "http://vsgg.co.kr",
                        "vsgg.co.kr"
                )   // 허용할 출처
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 HTTP method
                .allowedHeaders("*")  // 모든 헤더 허용
                .exposedHeaders("Authorization", "Content-Type")  // 노출할 헤더
                .allowCredentials(true)  // 쿠키 인증 요청 허용
                .maxAge(3600);  // 원하는 시간만큼 pre-flight 리퀘스트를 캐싱
    }
}
