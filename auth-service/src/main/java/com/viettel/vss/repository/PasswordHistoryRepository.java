package com.viettel.vss.repository;

import com.viettel.vss.base.BaseRepository;
import com.viettel.vss.entity.PasswordHistoryEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PasswordHistoryRepository extends BaseRepository<PasswordHistoryEntity, Long> {
    @Query("SELECT ph FROM PasswordHistoryEntity ph " +
            "JOIN UserEntity u ON ph.userId = u.id " +
            "WHERE ph.isDeleted = 0 " +
            "AND u.isDeleted = 0 " +
            "AND u.id = :userId " +
            "ORDER BY ph.id DESC")
    List<PasswordHistoryEntity> findOldLastPassword(Long userId);
}
