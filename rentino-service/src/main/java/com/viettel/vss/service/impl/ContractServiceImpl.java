package com.viettel.vss.service.impl;

import com.viettel.vss.constant.BusinessExceptionCode;
import com.viettel.vss.dto.attach_file.AttachFileDTO;
import com.viettel.vss.entity.*;
import com.viettel.vss.exception.BusinessException;
import com.viettel.vss.repository.*;
import com.viettel.vss.service.ContractService;
import com.viettel.vss.service.MinioService;
import com.viettel.vss.service.WordToPdfService;
import com.viettel.vss.util.DateUtil;
import com.viettel.vss.util.FileProcessing;
import com.viettel.vss.util.MessageCommon;
import com.viettel.vss.util.StringUtilsJr;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class ContractServiceImpl implements ContractService {
    @Autowired
    MinioService minioService;

    @Value("${jod.tmpFile}")
    private String tempFolderPath;

    @Value("${minio.endPoint}")
    private String endpoint;

    @Value("${minio.bucketName.process}")
    private String bucketName;

    @Autowired
    private SequenceRepo sequenceRepo;


    @Autowired
    private SystemConfigRepository systemConfigRepository;

    @Autowired
    private DeviceInfoRepository deviceInfoRepository;

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private UserBorrowRepository UserBorrowRepository;
    @Autowired
    MessageCommon messageCommon;

    @Autowired
    UserEkycRepository userEkycRepository;

    @Autowired
    EkycRepository ekycRepository;


    @Override
    public AttachFileDTO createContract(String phone, String transactionId) {


        Path temFoldder = Path.of(tempFolderPath);
        SystemConfig systemConfig = systemConfigRepository.findFirstByCodeAndIsDeleted("CONTRACT_FILE", 0);
        String contract = endpoint.concat(bucketName).concat("/").concat(systemConfig.getValue());
        Date date = new Date();
        String fileName = "contract".concat("_").concat(phone).concat("_").concat(String.valueOf(date.getTime()));
        Path outputTempFile = null;
        Path outputGenerateFile = null;
        Path outputPDFFile = Path.of(tempFolderPath, fileName.concat(".pdf"));
        AttachFileDTO attachFileDTO = new AttachFileDTO();
        try {
            System.out.println("generate file "+ fileName.concat(".docx"));
            outputTempFile = FileProcessing.downloadToTempDir(contract, fileName.concat(".docx"), temFoldder);
            outputGenerateFile = FileProcessing.downloadToTempDir(contract, fileName.concat("_generate.docx"), temFoldder);
            System.out.println("generate output file before generate file"+ outputGenerateFile.getFileName().toString());
            FileProcessing.generateFile(outputTempFile, outputGenerateFile, createMap(phone, transactionId));
            WordToPdfService.convert(outputGenerateFile, outputPDFFile);
            attachFileDTO.setFileName(outputPDFFile.getFileName().toString());
            byte[] bytes = Files.readAllBytes(outputPDFFile);
            attachFileDTO.setInputStream(new ByteArrayInputStream(bytes));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                Files.deleteIfExists(outputTempFile);
                Files.deleteIfExists(outputPDFFile);
                Files.deleteIfExists(outputGenerateFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return attachFileDTO;
    }

    private Map<String, String> createMap(String phone, String transactionId) {

        Map<String, String> vars = new HashMap<>();
        vars.put("${contract_no}", sequenceRepo.getSequenceNo());

        Optional<UserEntity> userEntity = usersRepository.findByPhone(phone);
        if (userEntity.isEmpty()) {
            throw new BusinessException(BusinessExceptionCode.ERROR_USER_NOTFOUND,
                    messageCommon.getMessage(BusinessExceptionCode.ERROR_USER_NOTFOUND));
        }
        UserEntity user = userEntity.get();
        DeviceInfo deviceInfo = deviceInfoRepository.findFirstByUserIdAndTransactionIdOrderByCreatedDate(user.getId(), transactionId);
        if (deviceInfo == null) {
            throw new BusinessException(BusinessExceptionCode.DEVICE_NOT_FOUND,
                    messageCommon.getMessage(BusinessExceptionCode.DEVICE_NOT_FOUND));
        }

        UserBorrow userBorrow = UserBorrowRepository.findFirstByUserIdAndTransactionOrderByCreatedDate(user.getId(), transactionId);
        if (userBorrow == null) {
            throw new BusinessException(BusinessExceptionCode.BORROW_NOT_FOUND,
                    messageCommon.getMessage(BusinessExceptionCode.BORROW_NOT_FOUND));
        }


        Optional<UserEkyc> ekyc = userEkycRepository.findFirstByUserIdAndTransactionIdOrderByCreatedDateDesc(user.getId(), transactionId);
        if (userEntity.isEmpty()) {
            throw new BusinessException(BusinessExceptionCode.OCR_NOT_FOUND,
                    messageCommon.getMessage(BusinessExceptionCode.OCR_NOT_FOUND));
        }

        Optional<EkycInfo> ekycInfo = ekycRepository.findFirstByUserIdAndIsDeletedOrderByCreatedDateDesc(user.getId(), 0);
        if (ekycInfo.isEmpty()) {
            throw new BusinessException(BusinessExceptionCode.USER_NOT_EKYC,
                    messageCommon.getMessage(BusinessExceptionCode.USER_NOT_EKYC));
        }

        EkycInfo userEkyc = ekycInfo.get();
        Date currentDate = new Date();

        vars.put("${current_day}", new SimpleDateFormat("dd").format(currentDate));
        vars.put("${current_month}", new SimpleDateFormat("MM").format(currentDate));
        vars.put("${current_year}", new SimpleDateFormat("yyyy").format(currentDate));
        vars.put("${pawn_party_name}", userEkyc.getFullName());
        vars.put("${pawn_party_address}", userEkyc.getAddress());
        vars.put("${pawn_party_phone}", user.getPhone());
        vars.put("${pawn_party_biz_no}", "");
        vars.put("${pawn_party_biz_issue_place}", "");
        vars.put("${pawn_party_biz_issue_date}", "");
        vars.put("${pawn_party_rep_name}", "");
        vars.put("${pawn_party_rep_title}", "");

        vars.put("${pawn_party_id_no}", userEkyc.getIdCard());
        vars.put("${pawn_party_issue_place}", userEkyc.getIssuedAt());
        vars.put("${pawn_party_id_issue_date}", DateUtil.convertDateToString(userEkyc.getIssueDate(), "dd/MM/yyyy"));

        vars.put("${pawn_party_bank_account}", "");
        vars.put("${pawn_party_bank_name}", "");

//        Device info
        String type = "Điện thoại di động";
        if (deviceInfo.getModel().equalsIgnoreCase("ipad")) {
            type = "Máy tính bảng";
        }
        vars.put("${type}", type);
        vars.put("${device_name}", deviceInfo.getDeviceName().toUpperCase());
        vars.put("${branch}", deviceInfo.getModel());
        vars.put("${price}", StringUtilsJr.currencyToText(deviceInfo.getPrice()));
        vars.put("${ram}", deviceInfo.getTotalRam());
        vars.put("${storage}", deviceInfo.getStorage());
        vars.put("${borrow}", StringUtilsJr.currencyToText(userBorrow.getValueBorrow()));
        vars.put("${borrow_from}", DateUtil.convertDateToString(userBorrow.getBorrowFromDate(), "dd/MM/yyyy"));
        vars.put("${borrow_to}", DateUtil.convertDateToString(userBorrow.getBorrowToDate(), "dd/MM/yyyy"));
        vars.put("${day_estimate}", new SimpleDateFormat("dd").format(userBorrow.getCreatedDate()));
        vars.put("${month_estimate}", new SimpleDateFormat("MM").format(userBorrow.getCreatedDate()));
        vars.put("${year_estimate}", new SimpleDateFormat("yyyy").format(userBorrow.getCreatedDate()));

        return vars;
    }
}
