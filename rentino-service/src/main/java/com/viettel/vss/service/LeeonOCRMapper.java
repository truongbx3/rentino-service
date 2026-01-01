package com.viettel.vss.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.vss.constant.BusinessExceptionCode;
import com.viettel.vss.dto.ocr.*;
import com.viettel.vss.exception.BusinessException;
import com.viettel.vss.util.MessageCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import static com.viettel.vss.constant.BusinessExceptionCode.INVALID_PHONE_NUMBER;
@Service
public class LeeonOCRMapper {

    @Value("${ekyc.card}")
    private String ekycUrl;

    @Value("${ekyc.liveness}")
    private String livenessUrl;

    @Value("${ekyc.faceMatching}")
    private String faceMatching;

    @Value("${ekyc.username}")
    private String username;

    @Value("${ekyc.token}")
    private String token;
    private RestTemplate restTemplate = new RestTemplate();
    private ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private MessageCommon messageCommon;
    private final ObjectMapper om = new ObjectMapper();

    public LeeonCardsResponse parse(String json) throws Exception {
        return om.readValue(json, LeeonCardsResponse.class);
    }

    public LeeonIdCardDtos.IdCardFrontInfo toFront(LeeonCardsResponse.CardDataItem item) {
        return om.convertValue(item.getInfo(), LeeonIdCardDtos.IdCardFrontInfo.class);
    }

    public LeeonIdCardDtos.IdCardBackInfo toBack(LeeonCardsResponse.CardDataItem item) {
        return om.convertValue(item.getInfo(), LeeonIdCardDtos.IdCardBackInfo.class);
    }


    public OcrResult OcrResult( String imgFrontB64, String imgBackB64) {
        LeeonCardsRequest reqBody = new LeeonCardsRequest(username, token, imgFrontB64, imgBackB64);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LeeonCardsRequest> entity = new HttpEntity<>(reqBody, headers);
        OcrResult ocrResult = null;
        try {
            ResponseEntity<LeeonCardsResponse> resp = restTemplate.exchange(
                    ekycUrl,
                    HttpMethod.POST,
                    entity,
                    LeeonCardsResponse.class
            );

            LeeonCardsResponse body = resp.getBody();
            if (body == null) {
                throw new RuntimeException("Empty response body from Leeon OCR");
            }

            // errorCode/errorMessage/TransactionId nằm ở top-level :contentReference[oaicite:6]{index=6}
            if (!"0" .equals(body.getErrorCode())) {
                throw new BusinessException(BusinessExceptionCode.OCR_FAIL,
                        messageCommon.getMessage(BusinessExceptionCode.OCR_FAIL).concat(" ").concat(body.getErrorMessage()));
            }
            ocrResult = new OcrResult();
            ocrResult.setTransactionId(body.getTransactionId());
            if (body.getData() != null) {
                for (LeeonCardsResponse.CardDataItem item : body.getData()) {
                    parseInfoTyped(item, ocrResult);
                }
            }

        } catch (RestClientException e) {
            throw new RuntimeException("Call Leeon OCR failed", e);
        }
        return ocrResult;
    }

    public LivenessResponse liveless(String portraitLeft, String portraitMid,String portraitRight) {
        LivenessRequest livenessRequest = new LivenessRequest(username, token, portraitLeft, portraitMid, portraitRight);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LivenessRequest> entity = new HttpEntity<>(livenessRequest, headers);
        LivenessResponse body = null;

        ResponseEntity<LivenessResponse> resp = restTemplate.exchange(
                livenessUrl,
                HttpMethod.POST,
                entity,
                LivenessResponse.class
        );

        body = resp.getBody();
        if (body == null) {
            throw new BusinessException("Empty response body", "UNKNOWN", null);
        }

        return body;
    }
    /**
     * Helper: convert “info” của item theo type
     */
    public void parseInfoTyped(LeeonCardsResponse.CardDataItem item, OcrResult ocrResult) {
        String type = item.getType();

        // danh sách type id-card theo tài liệu :contentReference[oaicite:7]{index=7}
        if ("12_id_card_front" .equals(type) || "9_id_card_front" .equals(type) || "chip_id_card_front" .equals(type)) {
            ocrResult.setFront(mapper.convertValue(item.getInfo(), LeeonIdCardDtos.IdCardFrontInfo.class));
            ocrResult.getFront().setType(type);
        }
        if ("12_id_card_back" .equals(type) || "9_id_card_back" .equals(type) || "chip_id_card_back" .equals(type)) {
            ocrResult.setBackInfo(mapper.convertValue(item.getInfo(), LeeonIdCardDtos.IdCardBackInfo.class));
            ocrResult.getBackInfo().setType(type);
        }
    }
    public LeeonFaceMatchingResponse faceMatching(
            String img1Base64,
            String img2Base64
    ) {
        LeeonFaceMatchingRequest req = new LeeonFaceMatchingRequest(username, token, img1Base64, img2Base64);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LeeonFaceMatchingRequest> entity = new HttpEntity<>(req, headers);

            ResponseEntity<LeeonFaceMatchingResponse> resp = restTemplate.exchange(
                    faceMatching,
                    HttpMethod.POST,
                    entity,
                    LeeonFaceMatchingResponse.class
            );

            LeeonFaceMatchingResponse body = resp.getBody();
            if (body == null) {
                throw new BusinessException("Empty response body", "UNKNOWN", null);
            }


            return body;

    }
}
