package com.viettel.vss.repository;

import com.viettel.vss.base.BaseRepository;
import com.viettel.vss.entity.UserServiceRegistrationEntity;

import java.util.Optional;

public interface UserServiceRegistrationRepository extends BaseRepository<UserServiceRegistrationEntity, Long>{

    Optional<UserServiceRegistrationEntity> findByServiceRegistrationIdAndUserIdAndIsActive(Long serviceRegistrationId, Long userId, Integer isActive);
}