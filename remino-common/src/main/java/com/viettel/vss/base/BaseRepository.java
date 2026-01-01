package com.viettel.vss.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BaseRepository<T extends BaseEntity,ID> extends JpaRepository<T,ID>, JpaSpecificationExecutor<ID> {
}
