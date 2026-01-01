package com.viettel.vss.dto.ocr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class LeeonFaceMatchingResponse {

    @JsonProperty("data")
    private Data data;

    @JsonProperty("errorCode")
    private String errorCode;

    @JsonProperty("errorMessage")
    private String errorMessage;

    @JsonProperty("TransactionId")
    private String transactionId;


    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {

        @JsonProperty("face1")
        private String face1;

        @JsonProperty("face1_score")
        private String face1Score; // "0.999796"

        @JsonProperty("face2")
        private String face2;

        @JsonProperty("face2_score")
        private String face2Score;

        @JsonProperty("invalidCode")
        private Integer invalidCode; // ví dụ 15

        @JsonProperty("invalidMessage")
        private String invalidMessage;

        @JsonProperty("match")
        private String match; // "1" hoặc "0"

        @JsonProperty("matching")
        private String matching; // "100.0"


        public boolean isMatchBoolean() {
            return "1".equals(match) || "true".equalsIgnoreCase(match);
        }

    }
}
