package com.viettel.vss.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeviceCheckRequest {
    public String transactionID;
    //     check chức năng
    public List<FunctionCheck> functionChecks;
    //    check ảnh qua gương
    public List<MirrorCheck> mirrorChecks;
//    tự khai nếu có
    public List<AdditionalCheck> additionalChecks;
}
