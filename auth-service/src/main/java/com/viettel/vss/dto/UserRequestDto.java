package com.viettel.vss.dto;

import lombok.Data;

@Data
public class UserRequestDto {
    private int page;
    private int size;
    private Long roleGroupId;
    private String userName;
    private String fullName;
    private Long statusId = 0L;
    private Boolean isDeleted = false;
}
