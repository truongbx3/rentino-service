package com.viettel.vss.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.viettel.vss.base.BaseController;
import com.viettel.vss.base.BaseService;
import com.viettel.vss.dto.ActionDto;
import com.viettel.vss.dto.ResponseDto;
import com.viettel.vss.dto.action.ActionCreateRequest;
import com.viettel.vss.dto.skill.SkillDto;
import com.viettel.vss.dto.skill.request.SkillCreatedRequest;
import com.viettel.vss.dto.skill.request.SkillFilterRequest;
import com.viettel.vss.dto.skill.response.PageResponse;
import com.viettel.vss.dto.skill.response.SkillResponseDto;
import com.viettel.vss.entity.Skill;
import com.viettel.vss.service.ActionService;
import com.viettel.vss.service.SkillService;
import com.viettel.vss.util.ResponseConfig;
import liquibase.pro.packaged.S;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("skill")
public class SkillController{
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("create")
    public ResponseEntity<ResponseDto<SkillResponseDto>> create(@RequestBody @Valid SkillCreatedRequest skillCreatedRequest) throws JsonProcessingException {
        return ResponseConfig.success(skillService.createSkill(skillCreatedRequest));
    }

    @PostMapping("update")
    public ResponseEntity<ResponseDto<SkillResponseDto>> update(@RequestBody @Valid SkillCreatedRequest skillCreatedRequest) throws JsonProcessingException {
        return ResponseConfig.success(skillService.updateSkill(skillCreatedRequest));
    }


    @PostMapping("deleted")
    public ResponseEntity<ResponseDto<String>> deleted(@RequestParam Long skillId) throws JsonProcessingException {
        skillService.deleteSkill(skillId);
        return ResponseConfig.success("Deleted");
    }

    @GetMapping("get-skill-by-id")
    public ResponseEntity<ResponseDto<SkillResponseDto>> getDetailSkill(@RequestParam Long skillId){
        return ResponseConfig.success(skillService.getSkill(skillId));
    }

    @GetMapping("get-all-skill")
    public ResponseEntity<ResponseDto<List<SkillResponseDto>>> getDetailSkill(){
        return ResponseConfig.success(skillService.getAllSkills());
    }

    @PostMapping("/search-skill")
    public ResponseEntity<PageResponse<SkillResponseDto>> searchSkills(@RequestBody SkillFilterRequest req) {
        return ResponseEntity.ok(skillService.searchSkills(req));
    }
}
