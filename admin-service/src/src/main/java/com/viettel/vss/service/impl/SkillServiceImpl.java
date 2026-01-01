package com.viettel.vss.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.vss.dto.skill.request.SkillCreatedRequest;
import com.viettel.vss.dto.skill.request.SkillFilterRequest;
import com.viettel.vss.dto.skill.response.PageResponse;
import com.viettel.vss.dto.skill.response.SkillResponseDto;
import com.viettel.vss.entity.*;
import com.viettel.vss.enums.AuditAction;
import com.viettel.vss.exception.BusinessException;
import com.viettel.vss.repository.*;
import com.viettel.vss.service.AuditLogService;
import com.viettel.vss.service.SkillService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.keycloak.util.JsonSerialization.mapper;

@Service
public class SkillServiceImpl implements SkillService {
    String targetName = "SKILL MANAGEMENT";

    private final SkillRepository skillRepository;

    private final ActionRepository actionRepository;

    private final AiAgentRepository aiAgentRepository;

    private final AgentSkillsRepository agentSkillsRepository;

    private final ActionSkillsRepository actionSkillsRepository;

    private final AuditLogService auditLogService;

    public SkillServiceImpl(SkillRepository skillRepository, ActionRepository actionRepository, AiAgentRepository aiAgentRepository, AgentSkillsRepository agentSkillsRepository, ActionSkillsRepository actionSkillsRepository, AuditLogService auditLogService) {
        this.skillRepository = skillRepository;
        this.actionRepository = actionRepository;
        this.aiAgentRepository = aiAgentRepository;
        this.agentSkillsRepository = agentSkillsRepository;
        this.actionSkillsRepository = actionSkillsRepository;
        this.auditLogService = auditLogService;
    }


    @Transactional
    @Override
    public SkillResponseDto createSkill(SkillCreatedRequest request) throws JsonProcessingException {

        // --- Kiểm tra trùng name trong DB ---
        boolean exists = skillRepository.existsByNameAndIsDeleted(request.getSkillName(), 0);
        if (exists) {
            throw new BusinessException(
                    "exception.message.duplicateSkillName",
                    "Tên kỹ năng '" + request.getSkillName() + "' đã tồn tại"
            );
        }

        Skill skill = new Skill();
        skill.setName(request.getSkillName());
        skill.setDescription(request.getDescription());

        // --- Actions ---
        List<ActionEntity> actions = actionRepository.findAllByIdInAndIsDeleted(request.getActionDtos(), 0);
        checkExistence(request.getActionDtos(), actions, ActionEntity::getId, "Action");

        List<SkillActionEntity> skillActions = actions.stream().map(action -> {
            SkillActionEntity sa = new SkillActionEntity();
            sa.setSkill(skill);
            sa.setAction(action);
            return sa;
        }).collect(Collectors.toList());
        // --- Agents ---
        List<AiAgent> aiAgents = aiAgentRepository.findAllByIdInAndIsDeleted(request.getAgentIds(), 0);
        checkExistence(request.getAgentIds(), aiAgents, AiAgent::getId, "AiAgent");

        List<AgentSkill> agentSkills = aiAgents.stream().map(agent -> {
            AgentSkill as = new AgentSkill();
            as.setAgent(agent);
            as.setSkill(skill);
            return as;
        }).collect(Collectors.toList());
        skill.setSkillActions(skillActions);
        skill.setAgentSkills(agentSkills);
        skillRepository.save(skill);


        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(new SkillResponseDto(skill));
        auditLogService.log(
                targetName,
                skill.getId().toString(),
                AuditAction.CREATE,
                payload
        );

        SkillResponseDto skillDto = new SkillResponseDto(skill);
        return skillDto;
    }


    private <T, ID> void checkExistence(List<ID> requestIds, List<T> entities, Function<T, ID> idMapper, String entityName) {
        // --- Loại bỏ trùng trong requestIds ---
        Set<ID> duplicates = requestIds.stream()
                .filter(id -> Collections.frequency(requestIds, id) > 1)
                .collect(Collectors.toSet());
        if (!duplicates.isEmpty()) {
            throw new BusinessException(
                    "exception.message.duplicateIds",
                    entityName + " bị trùng trong request: " + duplicates
            );
        }

        // --- Kiểm tra tồn tại trong DB ---
        Set<ID> foundIds = entities.stream()
                .map(idMapper)
                .collect(Collectors.toSet());
        List<ID> missingIds = requestIds.stream()
                .filter(id -> !foundIds.contains(id))
                .collect(Collectors.toList());
        if (!missingIds.isEmpty()) {
            throw new BusinessException(
                    "exception.message.invalidIds",
                    entityName + " không tồn tại: " + missingIds
            );
        }
    }



    @Transactional
    @Override
    public SkillResponseDto updateSkill(SkillCreatedRequest request) throws JsonProcessingException {

        Long skillId = request.getSkillId();
        if (skillId == null) {
            throw new BusinessException("exception.message.missingId", "Kỹ năng ID không được để trống");
        }

        // --- 1. Lấy Skill theo ID ---
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new BusinessException("exception.message.notFound", "Kỹ năng không tồn tại"));

        // Load quan hệ tách để tránh MultipleBagFetchException
        Skill skillWithActions = skillRepository.findWithActionsById(skillId).orElse(skill);
        Skill skillWithAgents = skillRepository.findWithAgentsById(skillId).orElse(skill);

        skill.setSkillActions(skillWithActions.getSkillActions());
        skill.setAgentSkills(skillWithAgents.getAgentSkills());

        // --- 2. Check trùng theo Name (trừ chính nó) ---
        if (skillRepository.existsByNameAndIdNot(request.getSkillName(), skillId)) {
            throw new BusinessException("exception.message.duplicateName",
                    "KỸ năng có tên đã tồn tại: " + request.getSkillName());
        }

        skill.setName(request.getSkillName());
        skill.setDescription(request.getDescription());

        // ======================================================
        // 3. clear old collection
        // ======================================================

        skill.getSkillActions().clear();
        skill.getAgentSkills().clear();

        // ======================================================
        // 5. Thêm mới Action
        // ======================================================
        List<ActionEntity> actions = actionRepository.findAllByIdInAndIsDeleted(request.getActionDtos(), 0);
        checkExistence(request.getActionDtos(), actions, ActionEntity::getId, "Action");

        List<SkillActionEntity> newSkillActions = actions.stream().map(action -> {
            SkillActionEntity sa = new SkillActionEntity();
            sa.setSkill(skill);
            sa.setAction(action);
            return sa;
        }).collect(Collectors.toList());

        skill.getSkillActions().addAll(newSkillActions);

        // ======================================================
        // 6. Thêm mới Agent
        // ======================================================
        List<AiAgent> aiAgents = aiAgentRepository.findAllByIdInAndIsDeleted(request.getAgentIds(), 0);
        checkExistence(request.getAgentIds(), aiAgents, AiAgent::getId, "AiAgent");

        List<AgentSkill> newAgentSkills = aiAgents.stream().map(agent -> {
            AgentSkill as = new AgentSkill();
            as.setAgent(agent);
            as.setSkill(skill);
            return as;
        }).collect(Collectors.toList());

        skill.getAgentSkills().addAll(newAgentSkills);

        // --- 8. Lưu skill ---
        skillRepository.save(skill);


        //save audit logs
        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(new SkillResponseDto(skill));
        auditLogService.log(
                targetName,
                skill.getId().toString(),
                AuditAction.UPDATE,
                payload
        );
        return new SkillResponseDto(skill);
    }



    @Transactional
    @Override
    public void deleteSkill(Long skillId) throws JsonProcessingException {
        if (skillId == null) {
            throw new BusinessException("exception.message.missingId",
                    "Kỹ năng ID không được để trống");
        }

        // 1. find skill
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new BusinessException(
                        "exception.message.notFound",
                        "Kỹ năng không tồn tại"
                ));

        // 3. Xóa skill
        skillRepository.delete(skill);

        //save audit logs
        Map<String, Object> payload = Map.of(
                "id", skill.getId(),
                "name", skill.getName(),
                "description", skill.getDescription()
        );
        String payloadJson = mapper.writeValueAsString(payload);
        auditLogService.log(
                targetName,
                skill.getId().toString(),
                AuditAction.DELETE,
                payloadJson
        );
    }

    @Override
    public SkillResponseDto getSkill(Long id) {
        Skill skill = skillRepository.findDetailByIdAndIsDeleted(id, 0)
                .orElseThrow(() -> new BusinessException(
                        "exception.message.notFound",
                        "Kỹ năng không tồn tại"
                ));

        return new SkillResponseDto(skill);
    }

    @Override
    public List<SkillResponseDto> getAllSkills() {
        List<Skill> skills = skillRepository.findAllByIsDeleted(0);

        return skills.stream()
                .map(SkillResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<SkillResponseDto> searchSkills(SkillFilterRequest req) {
        Page<Object[]> page = skillRepository.searchSkills(
                req.getName(), req.getDescription(), req.getCreatedFrom()
                , req.getCreatedTo(), req.getUpdatedFrom(), req.getUpdatedTo(), req.getPageable()
        );

        List<SkillResponseDto> content = page.getContent().stream().map(row -> {
            Long skillId = ((Number) row[0]).longValue();
            String skillName = (String) row[1];
            String skillDesc = (String) row[2];
            Date createdAt = (Date) row[3];
            Date updatedAt = (Date) row[4];
            String createdBy = (String) row[5];
            String updatedBy = (String) row[6];

            Skill skill = new Skill();
            skill.setId(skillId);
            skill.setName(skillName);
            skill.setDescription(skillDesc);
            skill.setCreatedDate(createdAt);
            skill.setUpdatedDate(updatedAt);
            skill.setCreatedBy(createdBy);
            skill.setUpdatedBy(updatedBy);

//            // Optional: load only actions & agents if cần
//            List<SkillActionEntity> actions = actionSkillsRepository.findBySkillId(skillId);
//            skill.setSkillActions(actions);
//
//            List<AgentSkill> agents = agentSkillsRepository.findBySkillId(skillId);
//            skill.setAgentSkills(agents);

            return new SkillResponseDto(skill);

        }).collect(Collectors.toList());


        PageResponse<SkillResponseDto> response = new PageResponse<>();
        response.setContent(content);
        response.setPage(page.getNumber());
        response.setSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setFirst(page.isFirst());
        response.setLast(page.isLast());

        return response;
    }

}
