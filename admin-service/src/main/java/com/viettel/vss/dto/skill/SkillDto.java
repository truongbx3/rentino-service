package com.viettel.vss.dto.skill;

import com.viettel.vss.base.BaseDto;
import com.viettel.vss.entity.Skill;
import lombok.Data;
import java.util.Date;

@Data
public class SkillDto extends BaseDto {
    private Long id;
    private String name;
    private String description;
    private Date createdAt;   // Ngày tạo
    private Date updatedAt;   // Ngày cập nhật
    private Long createdBy;            // ID người tạo
    private Long updatedBy;            // ID người chỉnh sửa
}
