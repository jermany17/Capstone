package com.example.capstone.controller;

import com.example.capstone.domain.User;
import com.example.capstone.dto.UserInfo;
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
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    // nickName 중복 확인 API
    @GetMapping("/check-nickname")
    public ResponseEntity<Map<String, String>> checkNickName(@RequestParam String nickName) {
        boolean exists = userService.isNickNameExists(nickName);

        if (exists) {
            return ResponseEntity.badRequest().body(Map.of("message", "이미 사용 중인 닉네임입니다.")); // 400
        } else {
            return ResponseEntity.ok(Map.of("message", "사용 가능한 닉네임입니다."));
        }
    }

    // userId 중복 확인 API
    @GetMapping("/check-userid")
    public ResponseEntity<Map<String, String>> checkUserId(@RequestParam String userId) {
        boolean exists = userService.isUserIdExists(userId);

        if (exists) {
            return ResponseEntity.badRequest().body(Map.of("message", "이미 사용 중인 아이디입니다.")); // 400
        } else {
            return ResponseEntity.ok(Map.of("message", "사용 가능한 아이디입니다."));
        }
    }

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
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);

            // 세션을 생성하고 SecurityContext를 저장
            HttpSession session = httpServletRequest.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

            Map<String, String> responseBody = new HashMap<>();
            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "로그인 실패: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @GetMapping("/auth/check")
    public ResponseEntity<Map<String, Object>> checkAuthStatus() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증되지 않은 경우 처리
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() instanceof String) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "로그인하지 않은 상태입니다."));
        }

        // 로그인된 사용자 정보 반환
        User user = (User) authentication.getPrincipal();
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "로그인 상태입니다.");
        response.put("nickName", user.getNickName());
        response.put("userId", user.getUserId());

        return ResponseEntity.ok(response);
    }

    // 로그인된 사용자 정보 조회 API (비밀번호 제외)
    @GetMapping("/userinfo")
    public ResponseEntity<UserInfo> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 로그인된 사용자 정보 반환 (비밀번호 제외)
        User user = (User) authentication.getPrincipal();
        UserInfo userInfo = new UserInfo(user);

        return ResponseEntity.ok(userInfo);
    }

    @DeleteMapping("/delete-account")
    public ResponseEntity<Map<String, String>> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal(); // 현재 로그인한 사용자 가져오기

        userService.deleteUser(user.getUserId()); // userId만 Service로 전달

        return ResponseEntity.ok(Map.of("message", "회원 탈퇴가 완료되었습니다."));
    }

    // 로그아웃 API = WebSecurityConfig에서 처리
}
