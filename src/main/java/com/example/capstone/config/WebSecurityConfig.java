package com.example.capstone.config;

import com.example.capstone.service.UserDetailService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailService userService;

    // 특정 HTTP 요청에 대한 보안 규칙
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/signup", "/login", "/logout").permitAll() // 접근 가능 URL
                        .anyRequest().authenticated()
                )
                .logout(logout -> logout
                         //.logoutUrl("/logout")  // 로그아웃 API 경로
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID") // JSESSIONID 쿠키 삭제

                        .logoutSuccessHandler((request, response, authentication) -> {

                            response.setContentType("application/json;charset=UTF-8"); // JSON 응답 설정

                            // 로그아웃 후 세션 확인
                            String sessionId = request.getSession(false) != null ? request.getSession(false).getId() : "";

                            // 세션이 남아 있으면 로그아웃 실패 처리
                            if (!sessionId.isEmpty()) {
                                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 상태 코드 반환
                                String jsonResponse = """
                                    {
                                        "sessionId": "%s",
                                        "message": "로그아웃 실패: 세션이 완전히 삭제되지 않았습니다."
                                    }
                                """.formatted(sessionId);
                                response.getWriter().write(jsonResponse);
                            } else {
                                response.setStatus(HttpServletResponse.SC_OK); // 200 OK 응답 반환
                                String jsonResponse = """
                                    {
                                        "sessionId": "%s",
                                        "message": "로그아웃 성공."
                                    }
                                """.formatted(sessionId);
                                response.getWriter().write(jsonResponse);
                            }

                            response.getWriter().flush();
                        })
                )
                .build();
    }

    // 인증 관리자 관련 설정
    @Bean
    public AuthenticationManager authenticationManager(BCryptPasswordEncoder bCryptPasswordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return new ProviderManager(authProvider);
    }

    // 비밀번호 인코더로 사용, 비밀번호 암호화 후 비교
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
