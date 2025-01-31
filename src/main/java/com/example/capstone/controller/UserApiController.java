package com.example.capstone.controller;

import com.example.capstone.dto.UserRequest;
import com.example.capstone.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    // 회원가입 API
    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signup(@RequestBody UserRequest request) {
        try {
            Long userId = userService.save(request);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "회원가입이 완료되었습니다.");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "회원가입 실패: " + e.getMessage()));
        }
    }

    // 로그인 API (세션 방식)
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserRequest request, HttpServletRequest httpServletRequest) {
        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(request.getUserId(), request.getUserPassword());

            Authentication authentication = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            HttpSession session = httpServletRequest.getSession();

            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("sessionId", session.getId()); // 세션 ID 포함
            responseBody.put("message", "로그인 성공.");

            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "로그인 실패: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    // 로그아웃 API = WebSecurityConfig에서 처리
}
