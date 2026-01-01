package com.viettel.vss.dto.serviceRegistration;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

public interface ServicesRegistrationResponse {
    String getCustomerName();
    String getCustomerEmail();
    String getCustomerPhone();
    String getCustomerAddress();
    String getToolCode();
    String getVersionsCode();
    Long getLimitUser();
    String getPeriod();
    Date getRegisterAt();
    String getDecidedBy();
    Date getDecidedAt();
    Integer getStatus();
    String getReason();
}
