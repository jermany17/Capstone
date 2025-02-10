package com.example.capstone.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.CookieSerializer;


@Configuration
public class CookieConfig {
    /*
    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();

        // SameSite=None 설정
        serializer.setSameSite("None");

        // Secure 속성 적용 (true = HTTPS 환경에서만 동작)
        serializer.setUseSecureCookie(true); // HTTP에서도 동작하도록 Secure 속성 비활성화

        return serializer;
    }
    */
}
