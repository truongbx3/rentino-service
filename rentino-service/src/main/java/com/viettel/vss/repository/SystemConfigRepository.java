package com.viettel.vss.repository;

import com.viettel.vss.base.BaseRepository;
import com.viettel.vss.entity.Interest;
import com.viettel.vss.entity.SystemConfig;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SystemConfigRepository extends BaseRepository<SystemConfig, Long>{


    SystemConfig findFirstByCodeAndIsDeleted(String code, int isDeleted);

}
