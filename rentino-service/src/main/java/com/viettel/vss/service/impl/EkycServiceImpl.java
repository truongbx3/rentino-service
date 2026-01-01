package com.viettel.vss.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.vss.constant.BusinessExceptionCode;
import com.viettel.vss.dto.ocr.LeeonFaceMatchingResponse;
import com.viettel.vss.dto.ocr.LivenessResponse;
import com.viettel.vss.dto.ocr.OcrResult;
import com.viettel.vss.entity.EkycInfo;
import com.viettel.vss.entity.UserEkyc;
import com.viettel.vss.entity.UserEntity;
import com.viettel.vss.exception.BusinessException;
import com.viettel.vss.repository.EkycRepository;
import com.viettel.vss.repository.UserEkycRepository;
import com.viettel.vss.repository.UsersRepository;
import com.viettel.vss.service.AttachFileService;
import com.viettel.vss.service.EkycService;
import com.viettel.vss.service.LeeonOCRMapper;
import com.viettel.vss.service.OpenAIService;
import com.viettel.vss.util.DataUtil;
import com.viettel.vss.util.DateUtil;
import com.viettel.vss.util.MessageCommon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
public class EkycServiceImpl implements EkycService {


    @Autowired
    LeeonOCRMapper leeonOCRMapper;

    @Autowired
    EkycRepository ekycRepository;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    MessageCommon messageCommon;

    @Autowired
    UserEkycRepository userEkycRepository;




    @Transactional
    @Override
    public OcrResult ekycCard(String transactionId,String phone, String frontImage, String backImage) throws IOException {
        Optional<UserEntity> userEntity = usersRepository.findByPhone(phone);
        if (userEntity.isEmpty()) {
            throw new BusinessException(BusinessExceptionCode.ERROR_USER_NOTFOUND,
                    messageCommon.getMessage(BusinessExceptionCode.ERROR_USER_NOTFOUND));
        }
        String frontB64 = OpenAIService.imageUrlToBase64(frontImage);
        String backB64 = OpenAIService.imageUrlToBase64(backImage);
        OcrResult ocrResult = leeonOCRMapper.OcrResult(frontB64, backB64);
        EkycInfo ekycInfo = new EkycInfo();
        ekycInfo.setIdCard(ocrResult.getFront().getId());
        ekycInfo.setType(ocrResult.getFront().getType());
        ekycInfo.setIdCard(ocrResult.getFront().getId());
        ekycInfo.setFullName(ocrResult.getFront().getName());
        ekycInfo.setDob(DateUtil.convertStringToDate(ocrResult.getFront().getDob(), "dd/MM/yyyy"));
        ekycInfo.setGender(ocrResult.getFront().getGender());
        ekycInfo.setNationality(ocrResult.getFront().getNationality());
        ekycInfo.setAddress(ocrResult.getFront().getAddress());
        ekycInfo.setHometown(ocrResult.getFront().getHometown());
        ekycInfo.setDueDate(DateUtil.convertStringToDate(ocrResult.getFront().getDueDate(), "dd/MM/yyyy"));
        ekycInfo.setIdentificationSign(ocrResult.getBackInfo().getIdentificationSign());
        ekycInfo.setIssueDate(DateUtil.convertStringToDate(ocrResult.getBackInfo().getIssueDate(), "dd/MM/yyyy"));
        ekycInfo.setIssuedAt(ocrResult.getBackInfo().getIssuedAt());
        ekycInfo.setUserId(userEntity.get().getId());
        ekycInfo.setTransactionId(transactionId);
        UserEkyc userEkyc = new UserEkyc();
        userEkyc.setTransactionId(transactionId);
        userEkyc.setOcrFront(frontImage);
        userEkyc.setOcrBack(backImage);
        userEkyc.setUserId(userEntity.get().getId());
        userEkycRepository.save(userEkyc);
        userEkyc.setStep("OCR");
        ekycRepository.save(ekycInfo);
        return ocrResult;
    }

    @Override
    public LivenessResponse livenessCheck(String transactionId,String phone, String portraitRight, String portraitLeft, String portraitMiddle) throws IOException {
        Optional<UserEntity> userEntity = usersRepository.findByPhone(phone);
        if (userEntity.isEmpty()) {
            throw new BusinessException(BusinessExceptionCode.ERROR_USER_NOTFOUND,
                    messageCommon.getMessage(BusinessExceptionCode.ERROR_USER_NOTFOUND));
        }
        Optional<UserEkyc>  ekyc = userEkycRepository.findFirstByUserIdAndTransactionIdOrderByCreatedDateDesc(userEntity.get().getId(), transactionId);
        if (userEntity.isEmpty()) {
            throw new BusinessException(BusinessExceptionCode.OCR_NOT_FOUND,
                    messageCommon.getMessage(BusinessExceptionCode.OCR_NOT_FOUND));
        }
        UserEkyc userEkyc = ekyc.get();
        userEkyc.setPortraitLeft(portraitLeft);
        userEkyc.setPortraitRight(portraitRight);
        userEkyc.setPortraitMid(portraitMiddle);
        userEkyc.setStep("LIVENESS");
        userEkycRepository.save(ekyc.get());
        String right = OpenAIService.imageUrlToBase64(portraitRight);
        String left = OpenAIService.imageUrlToBase64(portraitLeft);
        String middle = OpenAIService.imageUrlToBase64(portraitMiddle);
        LivenessResponse response = leeonOCRMapper.liveless( left,middle,right);
        userEkyc.setStatus(response.getErrorCode());
        userEkyc.setMessage(response.getErrorMessage());
        userEkycRepository.save(userEkyc);

        if (!"0" .equals(response.getErrorCode())) {

            throw new BusinessException(BusinessExceptionCode.LIVENESS_FAIL,
                    messageCommon.getMessage(BusinessExceptionCode.LIVENESS_FAIL).concat(" ").concat(response.getErrorMessage()));

        }

        return response;
    }

    @Override
    public LeeonFaceMatchingResponse faceMatching(String transactionId, String phone) throws IOException {

        Optional<UserEntity> userEntity = usersRepository.findByPhone(phone);
        if (userEntity.isEmpty()) {
            throw new BusinessException(BusinessExceptionCode.ERROR_USER_NOTFOUND,
                    messageCommon.getMessage(BusinessExceptionCode.ERROR_USER_NOTFOUND));
        }
        Optional<UserEkyc>  ekyc = userEkycRepository.findFirstByUserIdAndTransactionIdOrderByCreatedDateDesc(userEntity.get().getId(), transactionId);
        if (userEntity.isEmpty()) {
            throw new BusinessException(BusinessExceptionCode.OCR_NOT_FOUND,
                    messageCommon.getMessage(BusinessExceptionCode.OCR_NOT_FOUND));
        }
        UserEkyc userEkyc = ekyc.get();
        String portraitMid = OpenAIService.imageUrlToBase64(userEkyc.getPortraitMid());
        String ocrFront = OpenAIService.imageUrlToBase64(userEkyc.getOcrFront());
        LeeonFaceMatchingResponse response = leeonOCRMapper.faceMatching( ocrFront,portraitMid);
        userEkyc.setStatus(response.getErrorCode());
        userEkyc.setMessage(response.getErrorMessage());
        userEkyc.setStep("FACEMATCHING");
        userEkycRepository.save(userEkyc);
        if (!"0".equals(response.getErrorCode())) {
            throw new BusinessException(BusinessExceptionCode.FACEMATCHING_FAIL,
                    messageCommon.getMessage(BusinessExceptionCode.FACEMATCHING_FAIL).concat(" ").concat(response.getErrorMessage()));
        }
        return response;
    }

}
