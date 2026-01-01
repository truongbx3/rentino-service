package com.viettel.vss.repository;

import com.viettel.vss.base.BaseRepository;
import com.viettel.vss.dto.executableFunc.ExecutableFunctionDto;
import com.viettel.vss.dto.executableFunc.FilterExecutableFuncRequest;
import com.viettel.vss.entity.ExecutableFunction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExecutableFunctionRepository extends BaseRepository<ExecutableFunction, Long>{
    ExecutableFunction findByIdAndIsDeleted(Long id, int isDeleted);

    List<ExecutableFunction> findAllByIdInAndIsDeleted(List<Long> ids, int isDeleted);

    @Query("select distinct new com.viettel.vss.dto.executableFunc.ExecutableFunctionDto(" +
            "  ef.id, " +
            "  ef.keyCode, " +
            "  ef.name," +
            "  ef.dataInputType, " +
            "  ef.metaData," +
            "  ef.isActive," +
            "  ef.isDeleted," +
            "  ef.createdBy," +
            "  ef.createdDate," +
            "  ef.updatedBy," +
            "  ef.updatedDate, " +
            "  s.id, " +
            "  et.id," +
            "  s," +
            "  et) " +
            "from ExecutableFunction ef " +
            "join ef.system s " +
            "join ef.executionType et " +
            "where ef.isDeleted = :isDeleted" +
            "      and ef.id IN :ids ")
    List<ExecutableFunctionDto> getExecutableFuncById(List<Long> ids, int isDeleted);

    @Query("select distinct new com.viettel.vss.dto.executableFunc.ExecutableFunctionDto(" +
            "  ef.id, " +
            "  ef.keyCode, " +
            "  ef.name," +
            "  ef.dataInputType, " +
            "  ef.metaData," +
            "  ef.isActive," +
            "  ef.isDeleted," +
            "  ef.createdBy," +
            "  ef.createdDate," +
            "  ef.updatedBy," +
            "  ef.updatedDate, " +
            "  s.id, " +
            "  et.id," +
            "  s," +
            "  et) " +
            "from ExecutableFunction ef " +
            "join ef.system s " +
            "join ef.executionType et " +
            "where ef.isDeleted = :#{#filterExecutableFuncRequest.getIsDeleted()}" +
            "      and (:#{#filterExecutableFuncRequest.getIsActive()} is null or lower(ef.isActive) like lower(concat('%',:#{#filterExecutableFuncRequest.getIsActive()},'%'))) " +
            "      and (:#{#filterExecutableFuncRequest.keyCode} is null or lower(ef.keyCode) like lower(concat('%',:#{#filterExecutableFuncRequest.keyCode},'%'))) " +
            "      and (:#{#filterExecutableFuncRequest.name} is null or lower(ef.name) like lower(concat('%',:#{#filterExecutableFuncRequest.name},'%'))) " +
            "      and (COALESCE(:#{#filterExecutableFuncRequest.executionTypeId}) is null or ef.executionType.id IN :#{#filterExecutableFuncRequest.executionTypeId}) " +
            "      and (COALESCE(:#{#filterExecutableFuncRequest.systemId}) is null or ef.system.id IN :#{#filterExecutableFuncRequest.systemId}) " +
            "      and (:#{#filterExecutableFuncRequest.createdDate?.start} is null or ef.createdDate >=  :#{#filterExecutableFuncRequest.createdDate?.start}) " +
            "      and (:#{#filterExecutableFuncRequest.createdDate?.end}   is null or ef.createdDate <=  :#{#filterExecutableFuncRequest.createdDate?.end}) " +
            "      and (:#{#filterExecutableFuncRequest.updatedDate?.start} is null or ef.updatedDate >=  :#{#filterExecutableFuncRequest.updatedDate?.start}) " +
            "      and (:#{#filterExecutableFuncRequest.updatedDate?.end}   is null or ef.updatedDate <=  :#{#filterExecutableFuncRequest.updatedDate?.end}) ")
    Page<ExecutableFunctionDto> getExecutableFuncs(FilterExecutableFuncRequest filterExecutableFuncRequest, Pageable pageable);
}