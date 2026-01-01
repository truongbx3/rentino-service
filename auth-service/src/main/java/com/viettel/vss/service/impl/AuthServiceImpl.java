package com.viettel.vss.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.vss.config.sercurity.JwtTokenProvider;
import com.viettel.vss.constant.BusinessExceptionCode;
import com.viettel.vss.constant.LoginResource;
import com.viettel.vss.dto.*;
import com.viettel.vss.dto.request.ChangePasswordRequest;
import com.viettel.vss.dto.request.EmailCompanyCodeAppRequest;
import com.viettel.vss.dto.request.EmailCompanyCodeRequest;
import com.viettel.vss.dto.response.AuthResponse;
import com.viettel.vss.dto.response.TokenVerify;
import com.viettel.vss.entity.*;
import com.viettel.vss.exception.BusinessException;
import com.viettel.vss.repository.*;
import com.viettel.vss.service.AuthService;
import com.viettel.vss.service.ZnsClient;
import com.viettel.vss.util.*;
import io.jsonwebtoken.Claims;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private MessageCommon messageCommon;
    @Autowired
    public ModelMapper modelMapper;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordUtils passwordUtils;
    @Value("${jwt.secret}")
    private String JWT_SECRET;

    @Value("${jwt.expiration}")
    private long JWT_EXPIRATION;

    @Value("${jwt.refreshExp}")
    private long JWT_REFRESH_EXP;

    @Value("${jwt.startWith}")
    private String TOKEN_START_WITH;

    @Value("${jwt.job.secret}")
    private String JWT_JOB_SECRET;

    @Value("${jwt.job.expiration}")
    private long JWT_JOB_EXPIRATION;

    @Value("${jwt.job.username}")
    private String JWT_JOB_USERNAME;

    @Value("${jwt.job.password}")
    private String TOKEN_JOB_PASSWORD;

    @Value("${hashed.key}")
    private String keyPassword;

    @Value("${hashed.runner}")
    private String keyRunner;
    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${otp.expiration}")
    private long OTP_EXPIRATION;

    @Value("${notify.sendMail.url}")
    private String sendEmailUrl;

    @Value("${notify.sendNotify.url}")
    private String sendNotifyUrl;

    @Value("${notify.sendEmail.otpTemplate}")
    private String otpTemplate = "TEST-02";
    @Autowired
    private UserServiceRegistrationRepository userServiceRegistrationRepository;
    @Autowired
    private PasswordHistoryRepository passwordHistoryRepository;

    @Autowired
    private TokensRepository tokensRepository;

    @Autowired
    private CustomizeMessageCommon customizeMessageCommon;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private ZnsClient znsClient;

    @Transactional
    @Override
    public AuthResponse loginWeb(LoginDto dto) {

        if (DataUtil.isNullOrEmpty(dto.getPhone()) || DataUtil.isNullOrEmpty(dto.getPassword())) {
            throw new BusinessException(BusinessExceptionCode.USER_LOGIN_INVALID,
                    customizeMessageCommon.getMessage(BusinessExceptionCode.USER_LOGIN_INVALID));
        }
        UserEntity user = usersRepository.findByPhoneLoginWeb(dto.getPhone()).orElse(null);
        if (user == null || !PasswordUtils.verifyPassword(user.getSalt(), dto.getPassword(), user.getPassword())) {
            throw new BusinessException(BusinessExceptionCode.USER_LOGIN_INVALID,
                    customizeMessageCommon.getMessage(BusinessExceptionCode.USER_LOGIN_INVALID));
        }

        AuthResponse authResponse = new AuthResponse();
        Map<String, Object> claims = new HashMap<>();
        claims.put("login", LoginResource.WEB);
        UserDto userDto = DataUtil.convertObject(user, x -> modelMapper.map(x, UserDto.class));
        userDto.setPassword(null);
        userDto.setSalt(null);
        userDto.setId(null);

        authResponse.setUser(userDto);
        authResponse.getUser().setId(null);
        authResponse.setAccessToken(jwtTokenProvider.generateToken(dto.getPhone(), claims, JWT_EXPIRATION, JWT_SECRET));
        authResponse.setExpiresIn(JWT_EXPIRATION);

        logoutIfUserLoggedIn(dto.getPhone(),authResponse.getAccessToken());

        return authResponse;
    }

//    @Transactional
//    @Override
//    public AuthAppResponse loginApp(LoginAppDto dto) {
//        if (DataUtil.isNullOrEmpty(dto.getEmail()) || DataUtil.isNullOrEmpty(dto.getPassword()) || DataUtil.isNullOrEmpty(dto.getCompanyCode())) {
//            throw new BusinessException(BusinessExceptionCode.MISSING_REQUIRE_FIELD,
//                    customizeMessageCommon.getMessage(BusinessExceptionCode.MISSING_REQUIRE_FIELD));
//        }
//
//        // kiểm tra user - companycode - passs
//        UserEntity user = usersRepository.findByEmailActive(dto.getEmail()).orElse(null);
//        if (user == null || !PasswordUtils.verifyPassword(dto.getPassword().trim() + user.getSalt(), keyPassword, user.getPassword()) ||
//                (!dto.getTypeApp().equalsIgnoreCase(LoginResource.AT) && !dto.getTypeApp().equalsIgnoreCase(LoginResource.IDE))) {
//            throw new BusinessException(BusinessExceptionCode.INFO_LOGIN_INVALID,
//                    customizeMessageCommon.getMessage(BusinessExceptionCode.INFO_LOGIN_INVALID));
//        }
//
//
//        AuthAppResponse authResponse = new AuthAppResponse();
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("login", dto.getTypeApp());
//        claims.put("companyCode", dto.getCompanyCode());
//
//
//        String refreshToken =  UUID.randomUUID().toString();
//        Date expDate = new Date(System.currentTimeMillis() + JWT_REFRESH_EXP);
//
//        authResponse.setUser(DataUtil.convertObject(user, x -> modelMapper.map(x, UsersDto.class)));
//        authResponse.setAccessToken(jwtTokenProvider.generateToken(dto.getEmail(), claims, JWT_EXPIRATION, JWT_SECRET));
//        authResponse.setExpiresIn(JWT_EXPIRATION);
//        authResponse.setRefreshToken(refreshToken);
//        authResponse.setRefreshExpiresIn(JWT_REFRESH_EXP);
//        authResponse.setCompanyCode(dto.getCompanyCode());
//
//        logoutIfUserLoggedIn(dto.getEmail(), dto.getCompanyCode(), dto.getTypeApp(), authResponse.getAccessToken());
//
//        updateRefreshToken(user.getId(),refreshToken,expDate);
//
//        return authResponse;
//    }




    private void updateRefreshToken(Long userId, String refreshToken, Date expDate) {
        Optional<RefreshTokenEntity> entity = refreshTokenRepository.findByUserId(userId);
        if(entity.isEmpty()){
            RefreshTokenEntity newToken = new RefreshTokenEntity();
            newToken.setUserId(userId);
            newToken.setRefreshToken(refreshToken);
            newToken.setExpDate(expDate);
            refreshTokenRepository.save(newToken);
        }
        else{
            RefreshTokenEntity oldToken = entity.get();
            oldToken.setRefreshToken(refreshToken);
            oldToken.setExpDate(expDate);
            refreshTokenRepository.save(oldToken);
        }
    }



    @Override
    public JobTokenResponse tokenJob(JobTokenDto dto) {
        if (!DataUtil.safeEqual(dto.getUsername(), JWT_JOB_USERNAME) || !DataUtil.safeEqual(dto.getPassword(), TOKEN_JOB_PASSWORD)) {
            throw new BusinessException(BusinessExceptionCode.INFO_LOGIN_INVALID,
                    customizeMessageCommon.getMessage(BusinessExceptionCode.INFO_LOGIN_INVALID));
        }
        JobTokenResponse dataReturn = new JobTokenResponse();
        Map<String, Object> claims = new HashMap<>();
        claims.put("action", "JOB");
        dataReturn.setAccessToken(jwtTokenProvider.generateToken(createSubject(dto.getUsername(), dto.getPassword()), claims, JWT_JOB_EXPIRATION, JWT_JOB_SECRET));
        dataReturn.setExpiresIn(JWT_JOB_EXPIRATION);
        return dataReturn;
    }

    private String createSubject(String username, String password) {
        return username + password;
    }


    @Override
    public UserDto verifyToken(String token) {
        // check có tồn tại token không
        if(!tokensRepository.existsByAccessTokenAndIsDeleted(token.startsWith("Bearer ") ? token.substring(7) : token, 0)){
            throw new BusinessException(BusinessExceptionCode.LOGIN_SESSION_EXPIRED, customizeMessageCommon.getMessage(BusinessExceptionCode.LOGIN_SESSION_EXPIRED));
        }

        token = jwtTokenProvider.getAuthToken(token, TOKEN_START_WITH);

        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token, JWT_SECRET)) {

            Date date = jwtTokenProvider.getExpired(token, JWT_SECRET);
            if (date.compareTo( new Date()) < 0) {
                throw new BusinessException(BusinessExceptionCode.LOGIN_SESSION_EXPIRED, customizeMessageCommon.getMessage(BusinessExceptionCode.LOGIN_SESSION_EXPIRED));
            }
            // Lấy id user từ chuỗi jwt
            String phone = jwtTokenProvider.getUserAccountFromJWT(token, JWT_SECRET);

            if (phone == null) {
                throw new BusinessException(BusinessExceptionCode.USER_NOT_EXISTED,
                        customizeMessageCommon.getMessage(BusinessExceptionCode.USER_NOT_EXISTED));
            }

            UserEntity user = usersRepository.findByPhoneLoginWeb(phone).orElse(null);
            if (user == null) {
                throw new BusinessException(BusinessExceptionCode.USER_NOT_EXISTED,
                        customizeMessageCommon.getMessage(BusinessExceptionCode.USER_NOT_EXISTED));
            }

            UserDto userDto = DataUtil.convertObject(user, x -> modelMapper.map(x, UserDto.class));
            userDto.setPassword(null);
            userDto.setSalt(null);
            userDto.setId(null);
            return userDto;
        }
        throw new BusinessException(BusinessExceptionCode.TOKEN_INVALID,
                customizeMessageCommon.getMessage(BusinessExceptionCode.TOKEN_INVALID));
    }

    @Override
    public Boolean verifyJob(String token) {
        token = jwtTokenProvider.getAuthToken(token, TOKEN_START_WITH);
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token, JWT_JOB_SECRET)) {
            // Lấy id user từ chuỗi jwt
            String jwtDecode = jwtTokenProvider.getUserAccountFromJWT(token, JWT_JOB_SECRET);
            if (!DataUtil.safeEqual(jwtDecode, createSubject(JWT_JOB_USERNAME, TOKEN_JOB_PASSWORD))) {
                throw new BusinessException(BusinessExceptionCode.USER_NOT_EXISTED,
                        customizeMessageCommon.getMessage(BusinessExceptionCode.USER_NOT_EXISTED));
            }
            return true;
        }
        throw new BusinessException(BusinessExceptionCode.TOKEN_INVALID,
                customizeMessageCommon.getMessage(BusinessExceptionCode.TOKEN_INVALID));
    }


    @Override
    public void logoutWeb() {

    }



    private UserEntity checkUserAdminByEmail(String email, String companyCode) {
        return usersRepository.findByPhoneLoginWeb(email).orElse(null);
    }

    @Override
    public Void forGotPwWeb(String phone) {

        UserEntity user = usersRepository.findByPhoneLoginWeb(phone)
                .orElseThrow(() -> new BusinessException(BusinessExceptionCode.USER_NOT_EXISTED,
                        customizeMessageCommon.getMessage(BusinessExceptionCode.USER_NOT_EXISTED)));
        // tao OTP va gưi ve
        String otp = RandomUtils.generateOTP();
        OtpEntity otpEntity = new OtpEntity();
        otpEntity.setOtp(otp);
        otpEntity.setUserId(user.getId());
        otpEntity.setExpiredAt(LocalDateTime.now().plusSeconds(OTP_EXPIRATION));
        ZnsSendResponse znsSendResponse= znsClient.sendZnsOtp(otp, phone);
        otpEntity.setStatus(znsSendResponse.getStatus());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;
        try {
            json = objectMapper.writeValueAsString(znsSendResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        otpEntity.setResponse(json);
        otpRepository.save(otpEntity);
        if (znsSendResponse.getStatus().equalsIgnoreCase("FAILED")){
            throw new  BusinessException(BusinessExceptionCode.SEND_OTP_FAIL,
                    customizeMessageCommon.getMessage(BusinessExceptionCode.SEND_OTP_FAIL));
        }
        return null;
    }

    @Transactional
    @Override
    public Void changePwWeb(ChangePwDto dto) {

        UserEntity user = usersRepository.findByPhoneLoginWeb(dto.getPhone())
                .orElseThrow(() -> new BusinessException(BusinessExceptionCode.USER_NOT_EXISTED,
                        customizeMessageCommon.getMessage(BusinessExceptionCode.USER_NOT_EXISTED)));

        OtpEntity otpEntity = otpRepository.findByOtpAndIsDeleted(user.getId(),LocalDateTime.now()).stream().findFirst().orElse(null);

        if (otpEntity == null || !otpEntity.getOtp().equals(dto.getOtp())){
            throw  new BusinessException(BusinessExceptionCode.OTP_INVALID,
                    customizeMessageCommon.getMessage(BusinessExceptionCode.OTP_INVALID));
        }


        if (!DataUtil.safeEqual(dto.getNewPassword().trim(), dto.getRePassword().trim())) {
            throw new BusinessException(BusinessExceptionCode.PASSWORD_NOT_SAME,
                    customizeMessageCommon.getMessage(BusinessExceptionCode.PASSWORD_NOT_SAME));
        }

        // check password
        if (!passwordUtils.isValidPassword(dto.getNewPassword().trim())) {
            throw new BusinessException(BusinessExceptionCode.WRONG_PASSWORD_FORMAT,
                    customizeMessageCommon.getMessage(BusinessExceptionCode.WRONG_PASSWORD_FORMAT));
        }
        String newPasswordEncoded = passwordUtils.encode(user.getSalt(), dto.getNewPassword());
        if (newPasswordEncoded.equals(user.getPassword())) {
            throw new BusinessException(BusinessExceptionCode.PASSWORD_DUPLICATE,
                    customizeMessageCommon.getMessage(BusinessExceptionCode.PASSWORD_DUPLICATE));
        }
        List<PasswordHistoryEntity> passwordHistoryEntities = passwordHistoryRepository.findOldLastPassword(user.getId());
        if(!passwordHistoryEntities.isEmpty() && passwordHistoryEntities.get(0).getPassword().equals(newPasswordEncoded)){
            throw new BusinessException(BusinessExceptionCode.PASSWORD_DUPLICATE,
                    customizeMessageCommon.getMessage(BusinessExceptionCode.PASSWORD_DUPLICATE));
        }
        PasswordHistoryEntity passwordHistoryEntity = new PasswordHistoryEntity();
        passwordHistoryEntity.setUserId(user.getId());
        passwordHistoryEntity.setPassword(user.getPassword());
        user.setPassword(newPasswordEncoded);
        passwordHistoryRepository.save(passwordHistoryEntity);
        usersRepository.save(user);
        otpEntity.setIsDeleted(1);
        otpRepository.save(otpEntity);
        return null;
    }

    @Transactional
    @Override
    public Void checkEmailAndCompanyCode(EmailCompanyCodeRequest emailCompanyCodeRequest) {
//        UserEntity usersEntity = usersRepository.findUsersEntityByEmailAndCompanyCodeAndIsDeletedAndIsAdmin(emailCompanyCodeRequest.getEmail(), emailCompanyCodeRequest.getCompanyCode(), 0, 1)
//                .orElseThrow(() -> new BusinessException(BusinessExceptionCode.EMAIL_COMPANY_CODE_NOT_EXIST, customizeMessageCommon.getMessage(BusinessExceptionCode.EMAIL_COMPANY_CODE_NOT_EXIST)));
//
//        String otp = RandomUtils.generateOTP();
//        OtpEntity otpEntity = new OtpEntity();
//        otpEntity.setOtp(otp);
//        otpEntity.setUserId(usersEntity.getId());
//        otpEntity.setExpiredAt(LocalDateTime.now().plusSeconds(OTP_EXPIRATION));
//        otpRepository.save(otpEntity);
//        sendOtpEmail(otp, emailCompanyCodeRequest.getEmail(), emailCompanyCodeRequest.getCompanyCode());
        return null;
    }

    @Transactional
    @Override
    public Void checkEmailAndCompanyCodeApp(EmailCompanyCodeAppRequest emailCompanyCodeRequest) {
//        UserEntity user = usersRepository.findUsersEntityByEmailAndCompanyCodeAndIsDeleted(emailCompanyCodeRequest.getEmail(), emailCompanyCodeRequest.getCompanyCode(), 0).orElse(null);
//        if (user == null || (!emailCompanyCodeRequest.getTypeApp().equalsIgnoreCase(LoginResource.AT) && !emailCompanyCodeRequest.getTypeApp().equalsIgnoreCase(LoginResource.IDE))) {
//            throw new BusinessException(BusinessExceptionCode.INVALID_INFO_FORGOT_PASSWORD,
//                    customizeMessageCommon.getMessage(BusinessExceptionCode.INVALID_INFO_FORGOT_PASSWORD));
//        }
//
//        String otp = RandomUtils.generateOTP();
//        OtpEntity otpEntity = new OtpEntity();
//        otpEntity.setOtp(otp);
//        otpEntity.setUserId(user.getId());
//        otpEntity.setExpiredAt(LocalDateTime.now().plusSeconds(OTP_EXPIRATION));
//        otpRepository.save(otpEntity);
//        sendOtpEmail(otp, emailCompanyCodeRequest.getEmail(), emailCompanyCodeRequest.getCompanyCode());

        return null;
    }

    @Transactional
    @Override
    public Void changePwAppAfterLogin(HttpServletRequest request, ChangePasswordRequest changePasswordRequest) {
//        String bearerToken = extractBearerToken(request);
//        if (bearerToken != null) {
//            TokenVerify tokenVerify = verifyToken(bearerToken);
//            if (tokenVerify != null) {
//                UserEntity user = usersRepository.findUsersEntityByEmailAndCompanyCodeAndIsDeleted(tokenVerify.getUserName(), tokenVerify.getTenantCode(), 0)
//                        .orElseThrow(() -> new BusinessException(USER_NOT_FOUND, customizeMessageCommon.getMessage(USER_NOT_FOUND)));
//                if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getRetypeNewPassword())) {
//                    throw new BusinessException(PASSWORD_AND_RETYPE_PASSWORD_NOT_MATCH, customizeMessageCommon.getMessage(PASSWORD_AND_RETYPE_PASSWORD_NOT_MATCH));
//                }
//                if (!passwordUtils.isValidPassword(changePasswordRequest.getNewPassword())) {
//                    throw new BusinessException(INVALID_PASSWORD,
//                            customizeMessageCommon.getMessage(INVALID_PASSWORD));
//
//                }
//                if (!passwordUtils.verifyPassword(changePasswordRequest.getOldPassword() + user.getSalt(), keyPassword, user.getPassword())) {
//                    throw new BusinessException(WRONG_PASSWORD,
//                            customizeMessageCommon.getMessage(WRONG_PASSWORD));
//                }
//
//                if (changePasswordRequest.getOldPassword().equals(changePasswordRequest.getNewPassword())) {
//                    throw new BusinessException(BusinessExceptionCode.PASSWORD_DUPLICATE,
//                            customizeMessageCommon.getMessage(BusinessExceptionCode.PASSWORD_DUPLICATE));
//                }
//                String newPasswordEncoded = passwordUtils.encode(changePasswordRequest.getNewPassword() + user.getSalt(), keyPassword);
//                List<PasswordHistoryEntity> passwordHistoryEntities = passwordHistoryRepository.findOldLastPassword(user.getId());
//                if(!passwordHistoryEntities.isEmpty() && passwordHistoryEntities.get(0).getPassword().equals(newPasswordEncoded)){
//                    throw new BusinessException(BusinessExceptionCode.PASSWORD_DUPLICATE,
//                            customizeMessageCommon.getMessage(PASSWORD_DUPLICATE));
//                }
//                PasswordHistoryEntity passwordHistoryEntity = new PasswordHistoryEntity();
//                passwordHistoryEntity.setUserId(user.getId());
//                passwordHistoryEntity.setPassword(user.getPassword());
//                user.setPassword(newPasswordEncoded);
//                passwordHistoryRepository.save(passwordHistoryEntity);
//                usersRepository.save(user);
//            }
//        } else {
//            throw new BusinessException("400", "Error");
//        }
        return null;
    }


    @Transactional
    public void logoutIfUserLoggedIn(String phone,String accessToken){
        Optional<TokensEntity> optionalTokensEntity = tokensRepository.findByPhoneAndIsDeleted(phone, 0);
        if(optionalTokensEntity.isEmpty()){
            TokensEntity newToken = new TokensEntity();
            newToken.setPhone(phone);
            newToken.setAccessToken(accessToken);
            tokensRepository.save(newToken);
        }
        else{
            TokensEntity entity = optionalTokensEntity.get();
            entity.setAccessToken(accessToken);
            tokensRepository.save(entity);
        }
    }

}
