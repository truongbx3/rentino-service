package com.viettel.vss.dto.skill.response;

import com.viettel.vss.base.BaseDto;
import com.viettel.vss.entity.*;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class SkillResponseDto extends BaseDto {
    private Long id;
    private String name;
    private String description;
    private Date createdAt;   // Ngày tạo
    private Date updatedAt;   // Ngày cập nhật
    private String createdBy;            // ID người tạo
    private String updatedBy;            // ID người chỉnh sửa
    private List<ActionWithSkillDto> actions;
    private List<AgentWithSkillDto> agents;




    public SkillResponseDto(Skill skill) {
        this.id = skill.getId();
        this.name = skill.getName();
        this.description = skill.getDescription();
        this.createdAt = skill.getCreatedDate();
        this.updatedAt = skill.getUpdatedDate();
        this.createdBy = skill.getCreatedBy();
        this.updatedBy = skill.getUpdatedBy();
        this.actions = skill.getSkillActions()
                .stream()
                .map(sa -> new ActionWithSkillDto(sa.getAction(), skill))
                .collect(Collectors.toList());

        this.agents = skill.getAgentSkills()
                .stream()
                .map(as -> new AgentWithSkillDto(as.getAgent(), skill))
                .collect(Collectors.toList());
    }

}
