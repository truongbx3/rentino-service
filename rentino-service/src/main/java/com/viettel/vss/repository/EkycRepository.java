package com.viettel.vss.repository;

import com.viettel.vss.base.BaseRepository;
import com.viettel.vss.entity.AttachFile;
import com.viettel.vss.entity.EkycInfo;
import com.viettel.vss.entity.UserEkyc;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EkycRepository extends BaseRepository<EkycInfo, Long>{

    Optional<EkycInfo> findFirstByUserIdAndIsDeletedOrderByCreatedDateDesc(Long userId, int isDeleted);

}
