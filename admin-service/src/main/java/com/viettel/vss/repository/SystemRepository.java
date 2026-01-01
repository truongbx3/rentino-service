package com.viettel.vss.repository;

import com.viettel.vss.base.BaseRepository;
import com.viettel.vss.dto.system.FilterSystemRequest;
import com.viettel.vss.dto.system.SystemDto;
import com.viettel.vss.dto.system.SystemResponseDto;
import com.viettel.vss.entity.System;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemRepository extends BaseRepository<System, Long>{

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END " +
            "FROM System s " +
            "WHERE s.name = :name " +
            "AND s.isDeleted = 0 " +
            "AND (:id IS NULL OR s.id <> :id)")
    Boolean findByNameAndIsDeleted(@Param("name") String name, @Param("id") Long id);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END " +
            "FROM System s " +
            "WHERE s.baseUrl = :baseUrl " +
            "AND s.isDeleted = 0 " +
            "AND (:id IS NULL OR s.id <> :id)")
    Boolean findByBaseUrlAndIsDeleted(@Param("baseUrl") String baseUrl, @Param("id") Long id);

    System findByIdAndIsDeleted(Long id, int isDeleted);

    @Query("select distinct new com.viettel.vss.dto.system.SystemDto(" +
            "  s.id, " +
            "  s.name, " +
            "  s.baseUrl," +
            "  s.description, " +
            "  s.isActive," +
            "  s.isDeleted," +
            "  s.createdBy," +
            "  s.createdDate," +
            "  s.updatedBy," +
            "  s.updatedDate) " +
            "from System s " +
            "where s.isDeleted = :#{#filterSystemRequest.getIsDeleted()}" +
            "      and (:#{#filterSystemRequest.getIsActive()} is null or s.isActive = :#{#filterSystemRequest.getIsActive()}) " +
            "      and (:#{#filterSystemRequest.systemName} is null or lower(s.name) like lower(concat('%',:#{#filterSystemRequest.systemName},'%'))) " +
            "      and (:#{#filterSystemRequest.baseUrl} is null or lower(s.baseUrl) like lower(concat('%',:#{#filterSystemRequest.baseUrl},'%'))) " +
            "      and (:#{#filterSystemRequest.description} is null or lower(s.description) like lower(concat('%',:#{#filterSystemRequest.description},'%'))) " +
            "      and (:#{#filterSystemRequest.createdDate?.start} is null or s.createdDate >=  :#{#filterSystemRequest.createdDate?.start}) " +
            "      and (:#{#filterSystemRequest.createdDate?.end}   is null or s.createdDate <=  :#{#filterSystemRequest.createdDate?.end}) " +
            "      and (:#{#filterSystemRequest.updatedDate?.start} is null or s.updatedDate >=  :#{#filterSystemRequest.updatedDate?.start}) " +
            "      and (:#{#filterSystemRequest.updatedDate?.end}   is null or s.updatedDate <=  :#{#filterSystemRequest.updatedDate?.end}) ")
    Page<SystemDto> getSystems(FilterSystemRequest filterSystemRequest, Pageable pageable);

    List<System> findAllByIdInAndIsDeleted(List<Long> ids, int isDeleted);

    @Query("select distinct new com.viettel.vss.dto.system.SystemResponseDto(" +
            "  s.id, " +
            "  s.name, " +
            "  s.baseUrl," +
            "  s.description, " +
            "  s.isActive," +
            "  s.isDeleted," +
            "  s.createdBy," +
            "  s.createdDate," +
            "  s.updatedBy," +
            "  s.updatedDate, " +
            "  sa.credential, " +
            "  sa.authType, " +
            "  sa.fetchDuration) " +
            "from System s " +
            "join s.systemAuth sa " +
            "where s.isDeleted = 0" +
            "      and s.id IN :ids ")
    List<SystemResponseDto> getSystemById(List<Long> ids, int i);
}