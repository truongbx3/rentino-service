package com.viettel.vss.dto.action;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ActionListDto {
    private Long id;
    private String key;
    private String name;
    private List<ActionSkillDetail> actionSkillDetailList;
    private Long functionCount;
    private Date createdDate;
    private Date updatedDate;

    public ActionListDto(Long id, String key, String name, Long functionCount, Date createdDate, Date updatedDate){
        this.id = id;
        this.key = key;
        this.name = name;
        this.functionCount = functionCount;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

}
