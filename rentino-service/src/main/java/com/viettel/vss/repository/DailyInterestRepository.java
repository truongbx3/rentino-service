package com.viettel.vss.repository;

import com.viettel.vss.base.BaseRepository;
import com.viettel.vss.entity.DailyInterest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DailyInterestRepository extends BaseRepository<DailyInterest, Long>{

    @Query("SELECT f FROM DailyInterest f " +
            "WHERE f.type =:type  and f.isDeleted= 0  and f.fromDate <=:currentDate and  f.toDate >=:currentDate order by f.fromDate DESC ")
    List<DailyInterest> findDailyInterest(String type, Date currentDate);

}
