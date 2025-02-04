package com.example.capstone.controller;

import com.example.capstone.domain.User;
import com.example.capstone.dto.UpdateUserPassword;
import com.example.capstone.dto.UserInfo;
import com.example.capstone.dto.AddUser;
import com.example.capstone.dto.UserLogin;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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
    public ResponseEntity<Map<String, Object>> signup(@RequestBody AddUser request) {
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
    public ResponseEntity<Map<String, String>> login(@RequestBody UserLogin request, HttpServletRequest httpServletRequest) {
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
            responseBody.put("message", "로그인 성공");
            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "로그인 실패: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
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

    // 비밀번호 변경
    @PutMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(@RequestBody UpdateUserPassword updateUserPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();  // 현재 로그인한 사용자 가져오기

        // 현재 비밀번호가 일치하는지 확인
        if (!bCryptPasswordEncoder.matches(updateUserPassword.getCurrentPassword(), user.getUserPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "비밀번호가 일치하지 않습니다."));
        }

        // 새로운 비밀번호 암호화 후 서비스 호출
        String encodedNewPassword = bCryptPasswordEncoder.encode(updateUserPassword.getNewPassword());
        userService.updatePassword(user, encodedNewPassword);

        return ResponseEntity.ok(Map.of("message", "비밀번호가 성공적으로 변경되었습니다."));
    }

    // 회원 삭제
    @DeleteMapping("/delete-account")
    public ResponseEntity<Map<String, String>> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal(); // 현재 로그인한 사용자 가져오기

        userService.deleteUser(user.getUserId()); // userId만 Service로 전달

        return ResponseEntity.ok(Map.of("message", "회원 탈퇴가 완료되었습니다."));
    }

    // 로그아웃 API = WebSecurityConfig에서 처리
}
