package com.viettel.vss.controller;

import com.viettel.vss.dto.ResponseDto;
import com.viettel.vss.dto.ocr.LeeonFaceMatchingResponse;
import com.viettel.vss.dto.ocr.LivenessResponse;
import com.viettel.vss.dto.ocr.OcrResult;
import com.viettel.vss.service.EkycService;
import com.viettel.vss.util.ResponseConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("ekyc")
public class EKYCController {
    @Autowired
    EkycService ekycService;

    @PostMapping(value = "cards")
    public ResponseEntity<ResponseDto<OcrResult>> getEstimateCost(Principal principal, @RequestParam(required = true) String front, @RequestParam(required = true) String back,@RequestParam(required = true) String transactionId) throws IOException {

        return ResponseConfig.success(ekycService.ekycCard(transactionId,principal.getName(), front, back));
    }

    @PostMapping(value = "liveness")
    public ResponseEntity<ResponseDto<LivenessResponse>> checkLiveness(Principal principal, @RequestParam(required = true) String portraitRight, @RequestParam(required = true) String portraitLeft, @RequestParam(required = true) String portraitMiddle, @RequestParam(required = true) String transactionId) throws IOException {

        return ResponseConfig.success(ekycService.livenessCheck(transactionId,principal.getName(), portraitRight, portraitLeft,portraitMiddle));
    }

    @PostMapping(value = "faceMatching")
    public ResponseEntity<ResponseDto<LeeonFaceMatchingResponse>> faceMatching(Principal principal, @RequestParam(required = true) String transactionId) throws IOException {

        return ResponseConfig.success(ekycService.faceMatching(transactionId,principal.getName()));
    }
}
