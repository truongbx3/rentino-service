package com.viettel.vss.repository;

import com.viettel.vss.base.BaseRepository;
import com.viettel.vss.entity.Interest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface InterestRepository extends BaseRepository<Interest, Long>{

    @Query("SELECT f FROM Interest f " +
            "WHERE f.type =:type  and f.isDeleted= 0 and ( :#{#isFix}   IS NULL or f.isFixed =:#{#isFix})  and f.fromDate <=:currentDate and  f.toDate >=:currentDate order by f.fromDate DESC ")
    List<Interest> findInterest(String type, Date currentDate, Integer isFix);

}
