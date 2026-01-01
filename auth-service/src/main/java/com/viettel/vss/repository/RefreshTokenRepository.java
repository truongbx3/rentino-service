package com.viettel.vss.repository;

import com.viettel.vss.base.BaseRepository;
import com.viettel.vss.entity.RefreshTokenEntity;
import com.viettel.vss.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RefreshTokenRepository extends BaseRepository<RefreshTokenEntity, Long> {

    @Query(value = "SELECT rs from RefreshTokenEntity rs " +
            "WHERE rs.userId=:userId " )
    Optional<RefreshTokenEntity> findByUserId(Long userId);


    @Query(value = "SELECT u from UserEntity u " +
            "JOIN RefreshTokenEntity rs on rs.userId=u.id " +
            "WHERE rs.refreshToken=:token AND rs.expDate >= NOW() " )
    Optional<UserEntity> findByRefreshToken(String token);
}
