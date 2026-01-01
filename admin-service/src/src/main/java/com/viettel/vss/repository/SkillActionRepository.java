package com.viettel.vss.repository;

import com.viettel.vss.base.BaseRepository;
import com.viettel.vss.dto.action.ActionSkillDetail;
import com.viettel.vss.dto.action.ActionSkillDetailProjection;
import com.viettel.vss.entity.SkillActionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface SkillActionRepository extends BaseRepository<SkillActionEntity, Long> {

    @Query("SELECT sa FROM SkillActionEntity sa WHERE sa.isDeleted = 0 AND sa.action.id = :actionId")
    List<SkillActionEntity> findByActionId(@Param("actionId") Long actionId);

    @Query(value = "SELECT new com.viettel.vss.dto.action.ActionSkillDetail(sa.skill.id, sa.skill.name) " +
            "FROM SkillActionEntity sa " +
            "WHERE sa.isDeleted = 0 AND sa.action.id = :actionId")
    List<ActionSkillDetail> getActionSkillDetail(@Param("actionId") Long actionId);

    @Query(value = "SELECT sa.action.id as actionId, sa.skill.id as skillId, sa.skill.name as skillName " +
            "FROM SkillActionEntity sa " +
            "WHERE sa.isDeleted = 0 AND " +
            "COALESCE(:actionIds, NULL) IS NULL OR sa.action.id IN :actionIds")
    List<ActionSkillDetailProjection> getActionSkillDetailByActionIds( @Param("actionIds") List<Long> actionIds);
}
