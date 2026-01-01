package com.viettel.vss.dto.ocr;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LeeonCardsResponse {
    @JsonProperty("errorCode")
    private String errorCode;

    @JsonProperty("errorMessage")
    private String errorMessage;

    @JsonProperty("TransactionId")
    private String transactionId;

    @JsonProperty("data")
    private List<CardDataItem> data;

    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return errorMessage; }
    public String getTransactionId() { return transactionId; }
    public List<CardDataItem> getData() { return data; }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CardDataItem {
        @JsonProperty("type")
        private String type; // vd: 12_id_card_front, 12_id_card_back... :contentReference[oaicite:3]{index=3}

        // Có thể không có trong trường hợp PDF nhiều loại giấy tờ :contentReference[oaicite:4]{index=4}
        @JsonProperty("valid")
        private String valid;

        @JsonProperty("invalidCode")
        private String invalidCode;

        @JsonProperty("invalidMessage")
        private String invalidMessage;

        // info thay đổi theo từng loại giấy tờ => map “mềm”
        @JsonProperty("info")
        private Map<String, Object> info;

        public String getType() { return type; }
        public String getValid() { return valid; }
        public String getInvalidCode() { return invalidCode; }
        public String getInvalidMessage() { return invalidMessage; }
        public Map<String, Object> getInfo() { return info; }
    }
}
