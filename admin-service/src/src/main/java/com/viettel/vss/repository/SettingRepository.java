package com.viettel.vss.repository;

import java.util.List;

import com.viettel.vss.base.BaseRepository;
import com.viettel.vss.entity.Setting;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingRepository extends BaseRepository<Setting, Long>{
    List<Setting> findByUsernameAndIsDeletedAndTableName(String username, int isDeleted, String tableName);
    List<Setting> findByUsernameAndIsDeleted(String username, int isDeleted);
}