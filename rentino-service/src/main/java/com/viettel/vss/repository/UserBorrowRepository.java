package com.viettel.vss.repository;

import com.viettel.vss.base.BaseRepository;
import com.viettel.vss.entity.DeviceInfo;
import com.viettel.vss.entity.SystemConfig;
import com.viettel.vss.entity.UserBorrow;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBorrowRepository extends BaseRepository<UserBorrow, Long>{

    public UserBorrow findFirstByUserIdAndTransactionOrderByCreatedDate(Long userId, String transactionId);

}
