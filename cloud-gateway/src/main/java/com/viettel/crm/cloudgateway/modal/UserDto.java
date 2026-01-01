package com.viettel.crm.cloudgateway.modal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserDto {

    private long id;
    private String login;
    private String userName;
    private String tenantCode;
    private String token;
}
