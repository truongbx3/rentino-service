package com.viettel.vss.dto.ocr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OcrResult {
    LeeonIdCardDtos.IdCardFrontInfo front;
    LeeonIdCardDtos.IdCardBackInfo backInfo;
    String transactionId;
}
