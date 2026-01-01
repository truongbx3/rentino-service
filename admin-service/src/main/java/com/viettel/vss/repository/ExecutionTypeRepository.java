package com.viettel.vss.repository;

import com.viettel.vss.base.BaseRepository;
import com.viettel.vss.entity.ExecutionType;
import org.springframework.stereotype.Repository;

@Repository
public interface ExecutionTypeRepository extends BaseRepository<ExecutionType, Long>{
    ExecutionType findByIdAndIsDeleted(Long executionTypeId, int isDeleted);
}