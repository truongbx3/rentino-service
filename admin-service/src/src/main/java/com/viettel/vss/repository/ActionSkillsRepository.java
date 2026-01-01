package com.viettel.vss.repository;

import com.viettel.vss.base.BaseRepository;
import com.viettel.vss.entity.SkillActionEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActionSkillsRepository extends BaseRepository<SkillActionEntity, Long> {

    List<SkillActionEntity> findBySkillId(Long skillId);

    @Modifying
    @Query("DELETE FROM SkillActionEntity sa WHERE sa.skill.id = :skillId")
    void deleteBySkillId(@Param("skillId") Long skillId);
}
