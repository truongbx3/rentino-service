package com.viettel.vss.repository;

import com.viettel.vss.base.BaseRepository;
import com.viettel.vss.dto.ActionDto;
import com.viettel.vss.dto.action.ActionListDto;
import com.viettel.vss.dto.action.FilterActionRequest;
import com.viettel.vss.entity.ActionEntity;
import com.viettel.vss.entity.AiAgent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.viettel.vss.entity.SkillActionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActionRepository extends BaseRepository<ActionEntity, Long> {

    @Query(value = "SELECT 1 FROM ActionEntity a " +
            "WHERE (a.key = :#{#action.key} OR a.name = :#{#action.name}) " +
            "AND (coalesce(:#{#action.id}, NULL) IS NULL OR a.id <> :#{#action.id})")
    ActionEntity checkDuplicate(@Param("action") ActionDto action);

    // Lấy tất cả ActionEntity với id trong list và isDeleted = 0
    List<ActionEntity> findAllByIdInAndIsDeleted(List<Long> ids, int isDeleted);


    @Query(value = "SELECT new com.viettel.vss.dto.action.ActionListDto(" +
            "ae.id as id , ae.key as key, ae.name as name, COUNT (fa.id) as functionCount, " +
            "ae.createdDate as createdDate, ae.updatedDate as updatedDate) " +
            "FROM ActionEntity ae " +
            "JOIN FunctionActionEntity fa ON fa.action.id = ae.id " +
            "WHERE ae.isDeleted = 0 " +
            "AND (:#{#request.key} IS NULL OR LOWER(ae.key) LIKE LOWER(concat('%', :#{#request.key}, '%')) ) " +
            "AND (:#{#request.name} IS NULL OR LOWER(ae.name) LIKE LOWER(concat('%', :#{#request.name}, '%')) ) " +
            "AND (:#{#request.createdDate.start} IS NULL OR ae.createdDate >= :#{#request.createdDate.start} ) " +
            "AND (:#{#request.createdDate.end} IS NULL OR ae.createdDate <= :#{#request.createdDate.end} )" +
            "AND (:#{#request.updatedDate.start} IS NULL OR ae.updatedDate >= :#{#request.updatedDate.start} ) " +
            "AND (:#{#request.updatedDate.end} IS NULL OR ae.updatedDate <= :#{#request.updatedDate.end} )"
    )
    Page<ActionListDto> filterAction(FilterActionRequest request, Pageable pageable);
}
