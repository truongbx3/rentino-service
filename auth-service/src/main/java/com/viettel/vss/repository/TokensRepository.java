package com.viettel.vss.repository;

import com.viettel.vss.base.BaseRepository;
import com.viettel.vss.entity.TokensEntity;

import java.util.Optional;

public interface TokensRepository extends BaseRepository<TokensEntity, Long> {
    Optional<TokensEntity> findByPhoneAndIsDeleted(String phone,  Integer isDeleted);

    boolean existsByAccessTokenAndIsDeleted(String accessToken, Integer isDeleted);
}
