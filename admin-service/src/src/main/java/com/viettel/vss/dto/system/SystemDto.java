package com.viettel.vss.dto.system;

import com.viettel.vss.base.BaseDto;
import com.viettel.vss.entity.SystemAuth;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemDto extends BaseDto{
    private Long id;
    private String name;
    private String baseUrl;
    private String description;
    private Integer isActive = 1;
    private Integer isDeleted = 0;
    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;
    private SystemAuth systemAuth;

    public SystemDto (Long id, String name, String baseUrl, String description, Integer isActive, Integer isDeleted, String createdBy, Date createdDate, String updatedBy, Date updatedDate) {
        this.id = id;
        this.name = name;
        this.baseUrl = baseUrl;
        this.description = description;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.updatedBy = updatedBy;
        this.updatedDate = updatedDate;
    }

}