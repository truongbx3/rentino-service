package com.viettel.vss.service;

import com.viettel.vss.base.BaseService;
import com.viettel.vss.dto.UserDto;
import com.viettel.vss.entity.UserEntity;


public interface UserService extends BaseService<UserEntity, UserDto>{


    UserDto saveUser(UserDto userDto);

}