package com.viettel.vss.repository;

import com.viettel.vss.base.BaseRepository;
import com.viettel.vss.dto.action.ActionFunctionDetail;
import com.viettel.vss.entity.FunctionActionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FunctionActionRepository extends BaseRepository<FunctionActionEntity, Long> {

    @Query(value = "SELECT fa FROM FunctionActionEntity fa WHERE fa.isDeleted = 0 AND fa.actionId = :actionId")
    List<FunctionActionEntity> findByActionId(@Param("actionId") Long actionId);


    @Query(value = "SELECT new com.viettel.vss.dto.action.ActionFunctionDetail( f.id, f.name, f.keyCode, fa.orderIndex ) " +
            "FROM FunctionActionEntity fa " +
            "JOIN ExecutableFunction f ON fa.functionId = f.id AND f.isDeleted = 0 " +
            "WHERE fa.isDeleted = 0 AND fa.actionId = :actionId")
    List<ActionFunctionDetail> getActionFunctionDetail(@Param("actionId") Long actionId);
}
