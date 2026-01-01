package com.viettel.vss.base;

import com.viettel.vss.dto.RequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import java.util.List;

public interface BaseService<T extends BaseEntity, DTO extends BaseDto>{
    List<DTO> findByIds(List<Long> ids);
    List<DTO> findAll();
    void deleteById(Long id);
    void deleteByIds(List<Long> ids);
    void deleteData(List<T> ids);
    List<DTO> findAll(@Nullable Specification<T> spec);
    Page<DTO> findAll(@Nullable Specification<T> spec, Pageable pageable);
    Page<DTO> findAll(RequestDto requestDto);
    List<DTO> findAll(@Nullable Specification<T> spec, Sort sort);
    Long count(@Nullable Specification<T> spec);
    DTO saveObject(DTO data);
    void saveListObject(List<DTO> data);

}
