package com.viettel.vss.repository;

import com.viettel.vss.base.BaseRepository;
import com.viettel.vss.entity.AuthType;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthTypeRepository extends BaseRepository<AuthType, Long>{

    Boolean existsByCodeAndIsDeleted(String authType, int isDeleted);

    AuthType findByCodeAndIsDeleted(String authType, int isDeleted);
}