package com.viettel.vss.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.viettel.vss.dto.attach_file.TypeCheck;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CheckDeviceOpenAPI {
    @JsonProperty("mat_truoc")
    TypeCheck front;
    @JsonProperty("mat_sau")
    TypeCheck back;
    @JsonProperty("tong_ket")
    String summary;
}
