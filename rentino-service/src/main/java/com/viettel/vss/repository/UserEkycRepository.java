package com.viettel.vss.repository;

import com.viettel.vss.base.BaseRepository;
import com.viettel.vss.entity.SystemConfig;
import com.viettel.vss.entity.UserBorrow;
import com.viettel.vss.entity.UserEkyc;
import com.viettel.vss.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEkycRepository extends BaseRepository<UserEkyc, Long>{
    Optional<UserEkyc> findFirstByUserIdAndTransactionIdOrderByCreatedDateDesc(Long userId, String transactionId);

}
