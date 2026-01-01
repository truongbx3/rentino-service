package com.viettel.vss.service;

import com.viettel.vss.dto.*;
import com.viettel.vss.dto.request.ChangePasswordRequest;
import com.viettel.vss.dto.request.EmailCompanyCodeAppRequest;
import com.viettel.vss.dto.request.EmailCompanyCodeRequest;
import com.viettel.vss.dto.response.*;

import javax.servlet.http.HttpServletRequest;

public interface AuthService  {
    AuthResponse loginWeb(LoginDto dto);



    JobTokenResponse tokenJob(JobTokenDto dto);

    UserDto verifyToken(String token);

    Boolean verifyJob(String token);

    void logoutWeb();


    Void forGotPwWeb(String phone);

    Void changePwWeb(ChangePwDto dto);

    Void checkEmailAndCompanyCode(EmailCompanyCodeRequest emailCompanyCodeRequest);

    Void checkEmailAndCompanyCodeApp(EmailCompanyCodeAppRequest emailCompanyCodeRequest);

    Void changePwAppAfterLogin(HttpServletRequest request, ChangePasswordRequest changePasswordRequest);
}