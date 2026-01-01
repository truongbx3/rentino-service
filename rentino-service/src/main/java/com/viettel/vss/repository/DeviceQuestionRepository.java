package com.viettel.vss.repository;

import com.viettel.vss.base.BaseRepository;
import com.viettel.vss.entity.DevicePrice;
import com.viettel.vss.entity.DevicePriceDetail;
import com.viettel.vss.entity.DeviceQuestion;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceQuestionRepository extends BaseRepository<DeviceQuestion, Long>{


    List<DeviceQuestion> findAllByTypeAndIsDeletedOrderByOrderDesc(String type, int isDeleted);
}
