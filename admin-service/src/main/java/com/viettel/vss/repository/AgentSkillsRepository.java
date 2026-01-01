package com.viettel.vss.repository;

import com.viettel.vss.base.BaseRepository;
import com.viettel.vss.entity.AgentSkill;
import com.viettel.vss.entity.AiAgent;
import com.viettel.vss.entity.SkillActionEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgentSkillsRepository extends BaseRepository<AgentSkill, Long> {

    List<AgentSkill> findBySkillId(Long skillId);

    @Modifying
    @Query("DELETE FROM AgentSkill asg WHERE asg.skill.id = :skillId")
    void deleteBySkillId(@Param("skillId") Long skillId);
}
