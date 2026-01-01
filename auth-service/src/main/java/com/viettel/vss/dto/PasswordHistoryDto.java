package com.viettel.vss.dto;

import com.viettel.vss.base.BaseDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class PasswordHistoryDto extends BaseDto {
    @NotNull
    private Long id;

    private String password;

    private Long userId;

    private int isDeleted;


    private Date createdDate;


    private String createdBy;


    private Date updatedDate;


    private String updatedBy;
}
