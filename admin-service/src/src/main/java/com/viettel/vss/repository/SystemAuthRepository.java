package com.viettel.vss.repository;

import com.viettel.vss.base.BaseRepository;
import com.viettel.vss.entity.SystemAuth;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemAuthRepository extends BaseRepository<SystemAuth, Long>{

    @Query(value = "SELECT sa FROM SystemAuth sa WHERE sa.isDeleted = :isDeleted AND sa.system.id = :systemId")
    SystemAuth findBySystemIdAndIsDeleted(Long systemId, int isDeleted);
}