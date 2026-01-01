package com.viettel.vss.service.impl;

import com.viettel.vss.base.BaseServiceImpl;
import com.viettel.vss.constant.BusinessExceptionCode;
import com.viettel.vss.dto.UserDto;

import com.viettel.vss.dto.UsersDto;
import com.viettel.vss.entity.UserEntity;
import com.viettel.vss.exception.BusinessException;

import com.viettel.vss.repository.UserRepository;

import com.viettel.vss.service.UserService;
import com.viettel.vss.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl extends BaseServiceImpl<UserEntity, UserDto> implements UserService {

    @Autowired
    private MessageCommon messageCommon;

//    @Autowired
//    private NotifySendMail notifySendMail;

//    @Value("${notify.sendEmail.activeAccountAIG}")
//    private String activeAccountAIGTemplate;

//    @Value("${notify.sendEmail.login_link}")
//    private String loginUrl;

    private final UserRepository userRepository;


    private final PasswordUtils passwordUtils;


    public UserServiceImpl(UserRepository userRepository,
                           PasswordUtils passwordUtils) {
        super(userRepository, UserEntity.class, UserDto.class);
        this.userRepository = userRepository;
        this.passwordUtils = passwordUtils;
    }

    @Override
    @Transactional
    public UserDto saveUser(UserDto userDto) {
        Long id = userDto.getId();
        if(ObjectUtils.isEmpty(id)){
            if(StringUtils.isNullOrEmpty(userDto.getEmail())){
                throw new BusinessException(BusinessExceptionCode.USER_EMAIL_NOTNULL,
                        messageCommon.getMessage(BusinessExceptionCode.USER_EMAIL_NOTNULL));
            }
            if(StringUtils.isNullOrEmpty(userDto.getPhone())){
                throw new BusinessException(BusinessExceptionCode.USER_PHONE_NOTNULL,
                        messageCommon.getMessage(BusinessExceptionCode.USER_PHONE_NOTNULL));
            }
            if(StringUtils.isNullOrEmpty(userDto.getFirstName())){
                throw new BusinessException(BusinessExceptionCode.USER_FIRSTNAME_NOTNULL,
                        messageCommon.getMessage(BusinessExceptionCode.USER_FIRSTNAME_NOTNULL));
            }

            if(StringUtils.isNullOrEmpty(userDto.getLastName())){
                throw new BusinessException(BusinessExceptionCode.USER_LASTNAME_NOTNULL,
                        messageCommon.getMessage(BusinessExceptionCode.USER_LASTNAME_NOTNULL));
            }
            if(StringUtils.isNullOrEmpty(userDto.getPassword())){
                throw new BusinessException(BusinessExceptionCode.USER_PASS_NOTNULL,
                        messageCommon.getMessage(BusinessExceptionCode.USER_PASS_NOTNULL));
            }
            if (!passwordUtils.isValidPassword(userDto.getPassword().trim())) {
                throw new BusinessException(BusinessExceptionCode.WRONG_PASSWORD_FORMAT,
                        messageCommon.getMessage(BusinessExceptionCode.WRONG_PASSWORD_FORMAT));
            }
            List<UserEntity> existingUsers = userRepository.findByEmpPhoneOrEmail(userDto.getPhone(), userDto.getEmail(), userDto.getId());
            if (!existingUsers.isEmpty()) {
                throw new BusinessException(BusinessExceptionCode.USER_PHONE_EMAIL_EXIST,
                        messageCommon.getMessage(BusinessExceptionCode.USER_PHONE_EMAIL_EXIST));
            }
            String rawPassword = userDto.getPassword();
            String salt = RandomUtils.generateSalt();
            String encryptedPassword = passwordUtils.encode( salt,rawPassword);
            userDto.setPassword(encryptedPassword);
            userDto.setSalt(salt);
            userDto.setIsFirstLogin(true);
            return super.saveObject(userDto);
        }
        else{
//            Reset password
            UserEntity existingUser = userRepository.getUserById(id);
            if(ObjectUtils.isEmpty(existingUser)){
                throw new BusinessException(BusinessExceptionCode.USER_NOT_EXISTED,
                        messageCommon.getMessage(BusinessExceptionCode.USER_NOT_EXISTED));
            }
            String rawPassword = userDto.getPassword();
            String salt = RandomUtils.generateSalt();
            String encryptedPassword = passwordUtils.encode(rawPassword, salt);
            existingUser.setPassword(encryptedPassword);
            existingUser.setSalt(salt);
            StringUtils.updateIfNotNull(userDto.getEmail(), existingUser::setEmail);
            StringUtils.updateIfNotNull(userDto.getPhone(), existingUser::setPhone);
            StringUtils.updateIfNotNull(userDto.getFirstName(), existingUser::setFirstName);
            StringUtils.updateIfNotNull(userDto.getLastName(), existingUser::setLastName);
            return DataUtil.convertObject(userRepository.save(existingUser), x -> modelMapper.map(x, UserDto.class));
        }
    }



//    @Override
//    public void recoveryPassword(Long id) {
//        UserEntity user = userRepository.getUserById(id);
//        if(ObjectUtils.isEmpty(user)){
//            throw new BusinessException("Người dùng không tồn tại");
//        }
//        String rawPassword = RandomUtils.generatePassword();
//        String salt = RandomUtils.generateSalt();
//        String encryptedPassword = passwordUtils.encode(rawPassword, salt);
//        user.setPassword(encryptedPassword);
//        user.setSalt(salt);
//        user.setIsFirstLogin(true);
//        userRepository.save(user);
//        Map<String, Object> params = Map.of(
//                "full_name", user.getFullName(),
//                "user", user.getEmail(),
//                "password", rawPassword,
//                "login_link", loginUrl
//        );
//        notifySendMail.sendEmailTemplate(activeAccountAIGTemplate, user.getEmail(), user.getCompanyCode(), params);
//    }


}