package com.viettel.vss.dto.user;

import com.viettel.vss.dto.support.DateSupport;
import lombok.Data;

@Data
public class FilterUserRequest {
    private String username;
    private String fullName;
    private String email;
    private Long statusId;
    private DateSupport createdDate;
    private DateSupport updatedDate;
}
