package com.viettel.vss.repository;

import com.viettel.vss.base.BaseRepository;
import com.viettel.vss.entity.AttachFile;
import com.viettel.vss.entity.DeviceInfo;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceInfoRepository extends BaseRepository<DeviceInfo, Long>{


    public DeviceInfo findFirstByUserIdAndTransactionIdOrderByCreatedDate(Long userId, String transactionId);
}
