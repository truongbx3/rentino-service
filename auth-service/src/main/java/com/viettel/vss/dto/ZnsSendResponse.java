package com.viettel.vss.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZnsSendResponse {
    @JsonProperty("campaign_id")
    public String campaignId;

    @JsonProperty("created_at")
    public Date createdAt;

    @JsonProperty("delivery_status")
    public String deliveryStatus;

    @JsonProperty("delivery_time")
    public Date deliveryTime;

    @JsonProperty("error_code")
    public Integer errorCode;

    @JsonProperty("error_message")
    public String errorMessage;

    @JsonProperty("fee_main")
    public Integer feeMain;

    @JsonProperty("fee_token")
    public Integer feeToken;

    public String id;

    @JsonProperty("is_charged")
    public Boolean isCharged;

    @JsonProperty("is_development")
    public Boolean isDevelopment;

    @JsonProperty("journey_id")
    public String journeyId;

    @JsonProperty("msg_id")
    public String msgId;

    @JsonProperty("oa_id")
    public String oaId;

    public String phone;

    @JsonProperty("sent_time")
    public Date sentTime;

    @JsonProperty("sending_mode")
    public String sendingMode;

    @JsonProperty("shop_id")
    public String shopId;

    public String status;
    public String statusCode;

    @JsonProperty("template_data")
    public Map<String, Object> templateData;

    @JsonProperty("template_id")
    public Long templateId;

    public Integer timeout;

    @JsonProperty("tracking_id")
    public String trackingId;

    public String type;

    @JsonProperty("updated_at")
    public Date updatedAt;

    @JsonProperty("user_id")
    public String userId;


    // helper: thành công/thất bại theo error_code
    public boolean isSuccess() {
        return errorCode != null && errorCode == 0;
    }
}
