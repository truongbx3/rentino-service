package com.viettel.vss.dto.ocr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LivenessResponse {

    @JsonProperty("data")
    private Data data;

    @JsonProperty("errorCode")
    private String errorCode;

    @JsonProperty("errorMessage")
    private String errorMessage;

    @JsonProperty("TransactionId")
    private String TransactionId;

    @Getter
    @Setter
    @lombok.Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        @JsonProperty("invalidCode")
        private String invalidCode; // vd: 12_id_card_front, 12_id_card_back... :contentReference[oaicite:3]{index=3}

        // Có thể không có trong trường hợp PDF nhiều loại giấy tờ :contentReference[oaicite:4]{index=4}
        @JsonProperty("invalidMessage")
        private String invalidMessage;

        @JsonProperty("matching_mid_left")
        private String matchingMidLeft;

        @JsonProperty("matching_mid_right")
        private String matchingMidRight;

        @JsonProperty("valid")
        private String valid;
    }

}
