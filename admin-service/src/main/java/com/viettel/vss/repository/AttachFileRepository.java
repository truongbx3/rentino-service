package com.viettel.vss.repository;

import com.viettel.vss.base.BaseRepository;
import com.viettel.vss.entity.AttachFile;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttachFileRepository extends BaseRepository<AttachFile, Long>{
    Optional<AttachFile> findByFileNameAndSystem(String fileName, String system);
    Optional<AttachFile> findByFileName(String fileName);
}
