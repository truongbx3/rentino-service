package com.viettel.vss.dto.skill.response;

import com.viettel.vss.entity.AiAgent;
import com.viettel.vss.entity.Skill;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AgentWithSkillDto {
    private Long id;
    private String agentName;
    private Long skillId;

    public AgentWithSkillDto(AiAgent agent, Skill skill) {
        this.id = agent.getId();
        this.agentName = agent.getName();
        this.skillId = skill.getId();
    }
}
