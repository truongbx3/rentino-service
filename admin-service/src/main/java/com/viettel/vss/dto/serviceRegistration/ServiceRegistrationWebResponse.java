package com.viettel.vss.dto.serviceRegistration;

import lombok.Data;

@Data
public class ServiceRegistrationWebResponse {
    private Long id;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String customerAddress;
    private String toolCode;
    private String versionsCode;
    private Long limitUser;
    private String period;
}
