package com.viettel.vss.entity;

import com.viettel.vss.base.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "skills")
public class Skill extends BaseEntity{

	@Column(name = "name", nullable = false, length = 255)
	private String name;

	@Column(name = "description")
	private String description;

	@OneToMany(mappedBy = "skill", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SkillActionEntity> skillActions = new ArrayList<>();

	@OneToMany(mappedBy = "skill", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AgentSkill> agentSkills = new ArrayList<>();
}
