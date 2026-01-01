package com.viettel.vss.dto.ocr;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeeonFaceMatchingRequest {
    @JsonProperty("username")
    private String username;

    @JsonProperty("token")
    private String token;

    @JsonProperty("img1")
    private String img1; // Base64

    @JsonProperty("img2")
    private String img2; // Base64
}
