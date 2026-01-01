package com.viettel.vss.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZnsSendRequest {
    public String mode;

    @JsonProperty("oa_id")
    public String oaId;

    public String phone;

    @JsonProperty("sending_mode")
    public String sendingMode;

    @JsonProperty("template_data")
    public Map<String, Object> templateData;

    @JsonProperty("template_id")
    public Long templateId;
}
