package com.viettel.vss.controller;

import com.viettel.vss.dto.*;
import com.viettel.vss.dto.request.ChangePasswordRequest;
import com.viettel.vss.dto.request.EmailCompanyCodeAppRequest;
import com.viettel.vss.dto.request.EmailCompanyCodeRequest;
import com.viettel.vss.dto.response.AuthAppResponse;
import com.viettel.vss.dto.response.AuthResponse;
import com.viettel.vss.dto.response.TokenVerify;
import com.viettel.vss.service.AuthService;
import com.viettel.vss.util.ResponseConfig;
import org.checkerframework.common.reflection.qual.GetMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("auth")
public class AuthController  {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Đăng nhập
    @PostMapping(value="/login")
    public  ResponseEntity<ResponseDto<AuthResponse>> loginWeb(@RequestBody LoginDto dto) {
        return ResponseConfig.success(authService.loginWeb(dto));
    }

    // verify token
    @GetMapping(value="verifyToken")
    public  ResponseEntity<UserDto> verifyToken(@RequestParam String token) {
        return ResponseEntity.ok(authService.verifyToken(token));
    }

    // check mail -> tạo link
    @PostMapping(value="forgot-password")
    public  ResponseEntity<ResponseDto<Void>> forGotPwWeb(@RequestParam String phone) {
        return ResponseConfig.success(authService.forGotPwWeb(phone));
    }

    // đổi pass
    @PostMapping(value="change-password")
    public ResponseEntity<ResponseDto<Void>> changePwWeb(@RequestBody ChangePwDto dto) {

        return ResponseConfig.success(authService.changePwWeb(dto));
    }

    // đăng xuất
    @PostMapping(value="/web/logout")
    public  ResponseEntity<ResponseDto<Void>> logoutWeb() {
        authService.logoutWeb();
        return ResponseConfig.success(null);
    }

    // quên pass
    @PostMapping(value="public/web/check-email-company-code")
    public  ResponseEntity<ResponseDto<Void>> checkEmailAndCompanyCodeExist(@RequestBody EmailCompanyCodeRequest request) {
        return ResponseConfig.success(authService.checkEmailAndCompanyCode(request));
    }

    @PostMapping(value="public/app/check-email-company-code")
    public  ResponseEntity<ResponseDto<Void>> checkEmailAndCompanyCodeAppExist(@RequestBody EmailCompanyCodeAppRequest request) {
        return ResponseConfig.success(authService.checkEmailAndCompanyCodeApp(request));
    }


    @PostMapping(value = "app/change-password")
    public ResponseEntity<ResponseDto<Void>> changePwAppAfterLogin(HttpServletRequest request, @RequestBody ChangePasswordRequest changePasswordRequest) {

        return ResponseConfig.success(authService.changePwAppAfterLogin(request, changePasswordRequest));
    }
}