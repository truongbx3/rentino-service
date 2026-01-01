package com.viettel.vss.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.viettel.vss.base.BaseService;
import com.viettel.vss.dto.UserDto;
import com.viettel.vss.dto.skill.SkillDto;
import com.viettel.vss.dto.skill.request.SkillCreatedRequest;
import com.viettel.vss.dto.skill.request.SkillFilterRequest;
import com.viettel.vss.dto.skill.response.PageResponse;
import com.viettel.vss.dto.skill.response.SkillResponseDto;
import com.viettel.vss.entity.Skill;
import com.viettel.vss.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

public interface SkillService {

    SkillResponseDto createSkill(SkillCreatedRequest request) throws JsonProcessingException;

    SkillResponseDto updateSkill(SkillCreatedRequest request) throws JsonProcessingException;

    void deleteSkill(Long id) throws JsonProcessingException;

    SkillResponseDto getSkill(Long id);

    List<SkillResponseDto> getAllSkills();

    PageResponse<SkillResponseDto> searchSkills(SkillFilterRequest req);
}
