package com.viettel.vss.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailSenderRequest {
    Long id;
    Long templateId;
    String templateCode;
    String companyCode;
    String emailReceivers;
    String emailCC;
    String objects;
}
