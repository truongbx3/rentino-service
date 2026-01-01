package com.viettel.vss.repository;

import com.viettel.vss.base.BaseRepository;
import com.viettel.vss.entity.AgentSkill;
import com.viettel.vss.entity.AiAgent;
import com.viettel.vss.entity.SkillActionEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AiAgentRepository extends BaseRepository<AiAgent, Long> {
    // Lấy tất cả AiAgent với id trong list và isDeleted = 0
    List<AiAgent> findAllByIdInAndIsDeleted(List<Long> ids, int isDeleted);

}
