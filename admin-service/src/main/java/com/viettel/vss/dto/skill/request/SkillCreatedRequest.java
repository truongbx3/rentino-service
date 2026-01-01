package com.viettel.vss.dto.skill.request;

import com.viettel.vss.constant.BusinessExceptionCode;
import com.viettel.vss.constant.common.customValidator.NotBlankMessageCommon;
import com.viettel.vss.constant.common.customValidator.SizeMessageCommon;
import com.viettel.vss.dto.ActionDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class SkillCreatedRequest {
    private Long skillId;

    @NotBlankMessageCommon(code = BusinessExceptionCode.FIELD_REQUIRED, fieldName = "Tên kỹ năng")
    @SizeMessageCommon(code = BusinessExceptionCode.FILED_SIZE, min = 2, max = 255, fieldName = "Tên kỹ năng")
    private String skillName;

    @NotBlankMessageCommon(code = BusinessExceptionCode.FIELD_REQUIRED, fieldName = "AI Agent")
    private List<Long> agentIds;

    private String description;

    @NotBlankMessageCommon(code = BusinessExceptionCode.FIELD_REQUIRED, fieldName = "Hành động")
    private List<Long> actionDtos;
}
