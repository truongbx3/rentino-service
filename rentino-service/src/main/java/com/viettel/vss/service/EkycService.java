package com.viettel.vss.service;

import com.viettel.vss.dto.ocr.LeeonFaceMatchingResponse;
import com.viettel.vss.dto.ocr.LivenessResponse;
import com.viettel.vss.dto.ocr.OcrResult;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface EkycService {
    OcrResult ekycCard(String transactionId,String phone, String frontImage, String backImage) throws IOException;

    LivenessResponse livenessCheck(String transactionId,String phone, String portraitRight, String portraitLeft, String portraitMiddle) throws IOException;

    LeeonFaceMatchingResponse faceMatching(String transactionId, String phone) throws IOException;
}
