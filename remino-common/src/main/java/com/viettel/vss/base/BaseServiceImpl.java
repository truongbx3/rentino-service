package com.viettel.vss.base;


import com.viettel.vss.dto.RequestDto;
import com.viettel.vss.util.DataUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class BaseServiceImpl<T extends BaseEntity, DTO extends BaseDto> implements BaseService<T, DTO> {

    BaseRepository jpaRepository;
    Class<DTO> dtoClass;
    Class<T> entityClass;
    @Autowired
    public ModelMapper modelMapper;
    protected Logger LOG = LogManager.getLogger(this.getClass());
    public BaseServiceImpl(BaseRepository jpaRepository, Class<T> entityClass, Class<DTO> dtoClass) {
        this.dtoClass = dtoClass;
        this.entityClass = entityClass;
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<DTO> findByIds(List<Long> ids) {
        List<T> data = jpaRepository.findAllById(ids);
        return DataUtil.convertList(data, x -> modelMapper.map(x, dtoClass));
    }

    @Transactional
    @Override
    public DTO saveObject(DTO data) {
        T model = DataUtil.convertObject(data, x -> modelMapper.map(x, entityClass));
        return DataUtil.convertObject(jpaRepository.save(model), x -> modelMapper.map(x, dtoClass));
    }
    @Transactional
    @Override
    public void saveListObject(List<DTO> data) {
        List<T> model = DataUtil.convertList(data, x -> modelMapper.map(x, entityClass));
        jpaRepository.saveAll(model);
    }

    @Override
    public List<DTO> findAll() {
        List<T> data = jpaRepository.findAll();
        return DataUtil.convertList(data, x -> modelMapper.map(x, dtoClass));
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        List<T> data = jpaRepository.findAllById(ids);
        data.forEach( d -> {
            d.setIsDeleted(1);
        });
        jpaRepository.saveAll(data);
    }

    @Override
    public void deleteData(List<T> ids) {
        jpaRepository.deleteAll(ids);
    }

    @Override
    public List<DTO> findAll(@Nullable Specification<T> spec) {
        List<T> data = jpaRepository.findAll(spec);
        return DataUtil.convertList(data, x -> modelMapper.map(x, dtoClass));
    }

    @Override
    public Page<DTO> findAll(RequestDto requestDto) {
        VssSpecification<T> vssSpecification = new VssSpecification();
        vssSpecification.add(requestDto.getLsCondition());
        Pageable pageable = DataUtil.getPageable(requestDto);
        return this.findAll(vssSpecification, pageable);
    }

    @Override
    public Page<DTO> findAll(@Nullable Specification<T> spec, Pageable pageable) {
        Page<T> data = jpaRepository.findAll(spec, pageable);
        return data.map(x -> modelMapper.map(x, dtoClass));
    }

    @Override
    public List<DTO> findAll(@Nullable Specification<T> spec, Sort sort) {
        List<T> data = jpaRepository.findAll(spec, sort);
        return DataUtil.convertList(data, x -> modelMapper.map(x, dtoClass));
    }

    @Override
    public Long count(@Nullable Specification<T> spec) {
        return jpaRepository.count(spec);
    }

}
