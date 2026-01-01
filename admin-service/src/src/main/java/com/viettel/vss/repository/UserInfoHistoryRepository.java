package com.viettel.vss.repository;

import com.viettel.vss.base.BaseRepository;
import com.viettel.vss.entity.UserInfoHistory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInfoHistoryRepository extends BaseRepository<UserInfoHistory, Long>{
    @Query(value = "SELECT EXISTS ( " +
            "        SELECT 1 " +
            "        FROM user_info_history uih " +
            "        JOIN user u ON uih.email = u.email " +
            "        WHERE (COALESCE(:userIds, NULL) IS NOT NULL AND u.id IN (:userIds)) " +
            "    )", nativeQuery = true)
    boolean checkLogByUserIds(@Param("userIds") List<Long> userIds);
}