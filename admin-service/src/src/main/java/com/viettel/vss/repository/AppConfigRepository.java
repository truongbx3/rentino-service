package com.viettel.vss.repository;

import java.util.List;

import com.viettel.vss.base.BaseRepository;
import com.viettel.vss.entity.AppConfig;
import org.springframework.stereotype.Repository;

@Repository
public interface AppConfigRepository extends BaseRepository<AppConfig, Long>{
    List<AppConfig> findByConfigNameInAndIsDeleted(List<String> configName, int deleted);
}