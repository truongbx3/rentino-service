package com.viettel.vss.repository;

import com.viettel.vss.base.BaseRepository;
import com.viettel.vss.entity.DevicePrice;
import com.viettel.vss.entity.DevicePriceDetail;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DevicePriceDetailRepository extends BaseRepository<DevicePriceDetail, Long> {

    Optional<DevicePriceDetail> findFirstByDeviceCodeAndTypeAndIsDeleted(String deviceCode, String type, int isDeleted);


}
