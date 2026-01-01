package com.viettel.vss.repository;

import com.viettel.vss.base.BaseRepository;
import com.viettel.vss.entity.DeviceInfo;
import com.viettel.vss.entity.DevicePrice;
import com.viettel.vss.entity.DevicePriceDetail;
import com.viettel.vss.entity.DeviceQuestion;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DevicePriceRepository extends BaseRepository<DevicePrice, Long>{

    List<DevicePrice> findAllByIsDeleted(int isDeleted);

}
