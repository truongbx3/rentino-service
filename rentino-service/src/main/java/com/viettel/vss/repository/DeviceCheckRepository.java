package com.viettel.vss.repository;

import com.viettel.vss.base.BaseRepository;
import com.viettel.vss.entity.DeviceCheck;
import com.viettel.vss.entity.DeviceInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceCheckRepository extends BaseRepository<DeviceCheck, Long> {

    @Query("SELECT d FROM DeviceCheck d " +
            "WHERE d.userId =:userId  and d.transactionId=:transactionId and d.item in :item")
    public List<DeviceCheck> findItemCheck(Long userId, String transactionId, List<String> item);


    @Query("SELECT d FROM DeviceCheck d " +
            "WHERE d.userId =:userId  and d.transactionId=:transactionId and d.item in :item and d.value =:value ")
    public List<DeviceCheck> findFunctionCheck(Long userId, String transactionId, List<String> item,String value);


    @Query("SELECT d FROM DeviceCheck d " +
            "WHERE d.userId =:userId  and d.transactionId=:transactionId and d.item in :item")
    public List<DeviceCheck> findAdditionCheck(Long userId, String transactionId, List<String> item);

    @Query("SELECT d FROM DeviceCheck d " +
            "WHERE d.transactionId=:transactionId ")
    public List<DeviceCheck> findAllItemCheck( String transactionId);

}
