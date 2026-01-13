package com.viettel.vss.repository;

import com.viettel.vss.base.BaseRepository;
import com.viettel.vss.entity.Banks;
import com.viettel.vss.entity.DailyInterest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BanksRepository extends BaseRepository<Banks, Long>{

    @Query("SELECT b FROM Banks b " +
            "WHERE b.isDeleted= 0  order by b.name DESC ")
    List<Banks> getBanksInfo();

}
