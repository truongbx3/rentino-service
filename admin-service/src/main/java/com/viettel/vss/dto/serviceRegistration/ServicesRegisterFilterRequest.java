package com.viettel.vss.dto.serviceRegistration;

import lombok.Data;

import java.util.Date;

@Data
public class ServicesRegisterFilterRequest {

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
}
