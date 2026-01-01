package com.viettel.vss.dto.ocr;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LivenessRequest {
    @JsonProperty("username")
    private String username;

    @JsonProperty("token")
    private String token;

    @JsonProperty("portrait_left")
    private String portraitLeft;

    @JsonProperty("portrait_mid")
    private String portraitMid;

    @JsonProperty("portrait_right")
    private String portraitRight;
}
