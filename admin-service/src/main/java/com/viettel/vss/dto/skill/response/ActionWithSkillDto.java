package com.viettel.vss.dto.skill.response;

import com.viettel.vss.entity.ActionEntity;
import com.viettel.vss.entity.Skill;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActionWithSkillDto {
    private Long id;
    private String name;
    private Long skillId;

    public ActionWithSkillDto(ActionEntity action, Skill skill) {
        this.id = action.getId();
        this.name = action.getName();
        this.skillId = skill.getId();
    }
}
