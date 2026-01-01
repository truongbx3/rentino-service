package com.viettel.vss.dto.serviceRegistration;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ServicesRegistrationResponseDto {
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String customerAddress;
    private String toolCode;
    private String versionsCode;
    private Long limitUser;
    private String period;
    private Date registerAt;
    private String decidedBy;
    private Date decidedAt;
    private Integer status;
    private String reason;
    public ServicesRegistrationResponseDto(ServicesRegistrationResponse response) {
        this.customerName = response.getCustomerName();
        this.customerEmail = response.getCustomerEmail();
        this.customerPhone = response.getCustomerPhone();
        this.customerAddress = response.getCustomerAddress();
        this.toolCode = response.getToolCode();
        this.versionsCode = response.getVersionsCode();
        this.limitUser = response.getLimitUser();
        this.period = response.getPeriod();
        this.registerAt = response.getRegisterAt();
        this.decidedBy = response.getDecidedBy();
        this.decidedAt = response.getDecidedAt();
        this.status = response.getStatus();
        this.reason = response.getReason();
    }

}
