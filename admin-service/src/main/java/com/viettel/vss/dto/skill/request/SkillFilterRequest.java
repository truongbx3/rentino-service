package com.viettel.vss.dto.skill.request;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class SkillFilterRequest {

    private String name;
    private String description;

    private Date createdFrom;
    private Date createdTo;

    private Date updatedFrom;
    private Date updatedTo;

    private Integer page = 0;
    private Integer size = 20;

    public Pageable getPageable() {
        return PageRequest.of(page, size);
    }
}
