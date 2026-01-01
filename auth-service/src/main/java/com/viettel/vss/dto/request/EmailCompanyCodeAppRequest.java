package com.viettel.vss.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmailCompanyCodeAppRequest {
    private String email;
    private String companyCode;
    private String typeApp;
}
