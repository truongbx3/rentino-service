package com.viettel.vss.service.impl;

import com.viettel.vss.base.BaseServiceImpl;
import com.viettel.vss.constant.BusinessExceptionCode;
import com.viettel.vss.dto.*;
import com.viettel.vss.dto.attach_file.FunctionItem;
import com.viettel.vss.dto.attach_file.ImageItem;
import com.viettel.vss.dto.attach_file.TypeCheck;
import com.viettel.vss.entity.*;
import com.viettel.vss.exception.BusinessException;
import com.viettel.vss.repository.*;
import com.viettel.vss.service.DeviceInfoService;
import com.viettel.vss.service.OpenAIService;
import com.viettel.vss.util.DataUtil;
import com.viettel.vss.util.MessageCommon;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.analysis.function.Add;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DeviceInfoServiceImpl extends BaseServiceImpl<DeviceInfo, DeviceInfoDto> implements DeviceInfoService {

    @Value("${tieuchi:#{null}}")
    private String tieuchi;
    private static final Pattern TYPE_PATTERN = Pattern.compile("^LOAI_(\\d+)$");

    @Autowired
    private MessageCommon messageCommon;
    private final DeviceInfoRepository deviceInfoRepository;
    private final UsersRepository usersRepository;
    private final DeviceCheckRepository deviceCheckRepository;
    private final DevicePriceDetailRepository devicePriceDetailRepository;
    private final DeviceQuestionRepository deviceQuestionRepository;
    private OpenAIService openAIService;

    public DeviceInfoServiceImpl(DeviceInfoRepository deviceInfoRepository, UsersRepository usersRepository, DeviceCheckRepository deviceCheckRepository, DeviceQuestionRepository deviceQuestionRepository, OpenAIService openAIService, DevicePriceDetailRepository devicePriceDetailRepository) {
        super(deviceInfoRepository, DeviceInfo.class, DeviceInfoDto.class);
        this.deviceInfoRepository = deviceInfoRepository;
        this.usersRepository = usersRepository;
        this.deviceCheckRepository = deviceCheckRepository;
        this.openAIService = openAIService;
        this.deviceQuestionRepository = deviceQuestionRepository;
        this.devicePriceDetailRepository = devicePriceDetailRepository;
    }

    @Override
    public String generateUUid(String username) {
        StringJoiner joiner = new StringJoiner("-");
        joiner.add(username);
        joiner.add(UUID.randomUUID().toString().replace("-", ""));
        return joiner.toString();
    }

    @Override
    public String deviceCheck(DeviceCheckRequest deviceCheckRequest, String username) {
        Optional<UserEntity> userEntity = usersRepository.findByPhone(username);
        if (userEntity.isEmpty()) {
            throw new BusinessException(BusinessExceptionCode.ERROR_USER_NOTFOUND,
                    messageCommon.getMessage(BusinessExceptionCode.ERROR_USER_NOTFOUND));
        }
        List<FunctionCheck> functionChecks = deviceCheckRequest.getFunctionChecks();
        List<MirrorCheck> mirrorCheck = deviceCheckRequest.getMirrorChecks();
        List<AdditionalCheck> additionalChecks = deviceCheckRequest.getAdditionalChecks();
        List<DeviceCheck> lstDeviceChecks = new ArrayList<>();
        for (FunctionCheck functionCheck : functionChecks) {
            DeviceCheck funcCheck = new DeviceCheck();
            funcCheck.setUserId(userEntity.get().getId());
            funcCheck.setTransactionId(deviceCheckRequest.getTransactionID());
            funcCheck.setItem(functionCheck.getItem().toString());
            funcCheck.setValue(functionCheck.getValue() ? "1" : "0");
            lstDeviceChecks.add(funcCheck);
        }
        for (MirrorCheck MirrorCheck : mirrorCheck) {
            DeviceCheck mirCheck = new DeviceCheck();
            mirCheck.setUserId(userEntity.get().getId());
            mirCheck.setTransactionId(deviceCheckRequest.getTransactionID());
            mirCheck.setItem(MirrorCheck.getItem().toString());
            mirCheck.setValue(MirrorCheck.getValue());
            lstDeviceChecks.add(mirCheck);
        }
        for (AdditionalCheck additionalCheck : additionalChecks) {
            DeviceCheck addCheck = new DeviceCheck();
            addCheck.setUserId(userEntity.get().getId());
            addCheck.setTransactionId(deviceCheckRequest.getTransactionID());
            addCheck.setItem(additionalCheck.getCode());
            addCheck.setValue(additionalCheck.getValue());
            lstDeviceChecks.add(addCheck);
        }
        deviceCheckRepository.saveAll(lstDeviceChecks);
        return "";
    }

    @Override
    public String saveDeviceInfo(String username, DeviceInfoDto deviceInfoDtos) {
        Optional<UserEntity> userEntity = usersRepository.findByPhone(username);
        if (userEntity.isEmpty()) {
            throw new BusinessException(BusinessExceptionCode.ERROR_USER_NOTFOUND,
                    messageCommon.getMessage(BusinessExceptionCode.ERROR_USER_NOTFOUND));
        }
        deviceInfoDtos.setUserId(userEntity.get().getId());
        deviceInfoDtos.setModel(deviceInfoDtos.getModel().toLowerCase());
        deviceInfoDtos.setDeviceName(deviceInfoDtos.getDeviceName().toLowerCase());
        deviceInfoDtos.setDeviceCode(joinWithUnderscore(deviceInfoDtos.getDeviceName(), deviceInfoDtos.getTotalRam(), deviceInfoDtos.getStorage()));
        this.saveObject(deviceInfoDtos);
        return "";
    }

    @Override
    public DeviceInfoDto analyze(String userName, String transactionId) throws Exception {
        Optional<UserEntity> userEntity = usersRepository.findByPhone(userName);
        if (userEntity.isEmpty()) {
            throw new BusinessException(BusinessExceptionCode.ERROR_USER_NOTFOUND,
                    messageCommon.getMessage(BusinessExceptionCode.ERROR_USER_NOTFOUND));
        }
        UserEntity userEntity1 = userEntity.get();
        List<DeviceCheck> deviceChecks = deviceCheckRepository.findItemCheck(userEntity1.getId(), transactionId, List.of(ImageItem.FRONT_CAMERA.toString(), ImageItem.BACK_CAMERA.toString()));
        if (deviceChecks.size() != 2) {
            throw new BusinessException(BusinessExceptionCode.SCREENCHECK_NOT_ENOUGHT,
                    messageCommon.getMessage(BusinessExceptionCode.SCREENCHECK_NOT_ENOUGHT));
        }
        CheckDeviceOpenAPI checkDeviceOpenAPI = openAIService.analyze(splitImage(deviceChecks));
        DeviceInfo deviceInfo = deviceInfoRepository.findFirstByUserIdAndTransactionIdOrderByCreatedDate(userEntity1.getId(), transactionId);
        if (deviceInfo == null) {
            throw new BusinessException(BusinessExceptionCode.DEVICE_NOT_FOUND,
                    messageCommon.getMessage(BusinessExceptionCode.DEVICE_NOT_FOUND));
        }
        deviceInfo.setFrontCheck(checkDeviceOpenAPI.getFront().toString());
        deviceInfo.setBackCheck(checkDeviceOpenAPI.getBack().toString());
        deviceInfo.setSummary(checkDeviceOpenAPI.getSummary());
//        Kiem tra xem function check co ok khong
        List<DeviceCheck> functionCheck = deviceCheckRepository.findFunctionCheck(userEntity1.getId(), transactionId, Arrays.asList(FunctionItem.values()).stream().map(Enum::name).collect(Collectors.toList()), "0");
        if (functionCheck.size() > 0) {
            deviceInfo.setFunctionCheck(TypeCheck.LOAI_5.toString());
        } else {
            deviceInfo.setFunctionCheck(TypeCheck.LOAI_1.toString());
        }

        if (deviceInfo.getFrontCheck().equalsIgnoreCase(TypeCheck.LOAI_0.toString())) {
            deviceInfo.setFrontCheck(TypeCheck.LOAI_5.toString());
//            throw new BusinessException(BusinessExceptionCode.SCREENCHECK_FRONT_CHECK,
//                    messageCommon.getMessage(BusinessExceptionCode.SCREENCHECK_FRONT_CHECK));
        }
        if (deviceInfo.getBackCheck().equalsIgnoreCase(TypeCheck.LOAI_0.toString())) {
            deviceInfo.setBackCheck(TypeCheck.LOAI_5.toString());
//            throw new BusinessException(BusinessExceptionCode.SCREENCHECK_BACK_CHECK,
//                    messageCommon.getMessage(BusinessExceptionCode.SCREENCHECK_BACK_CHECK));
        }
//        addition check
        List<DeviceQuestion> deviceQuestions = deviceQuestionRepository.findAllByTypeAndIsDeletedOrderByOrderDesc("MOBILE", 0);
        List<String> lstAdd = deviceQuestions.stream().map(deviceQuestion -> deviceQuestion.getCode()).collect(Collectors.toList());
        List<DeviceCheck> additonCheck = deviceCheckRepository.findAdditionCheck(userEntity1.getId(), transactionId, lstAdd);
        List<String> lstValue = additonCheck.stream().map(DeviceCheck::getValue).collect(Collectors.toList());
        String maxAdditionCheck = maxType(lstValue);

//        String finalSummary = findMaxType(deviceInfo.getFrontCheck(), deviceInfo.getBackCheck(), deviceInfo.getFunctionCheck(),maxAdditionCheck);
        String finalSummary = maxType(Arrays.asList(deviceInfo.getFrontCheck(), deviceInfo.getBackCheck(), deviceInfo.getFunctionCheck(), maxAdditionCheck));
        deviceInfo.setAdditionCheck(maxAdditionCheck);
        deviceInfo.setFinalSummary(finalSummary);
        Optional<DevicePriceDetail> devicePriceDetail = devicePriceDetailRepository.findFirstByDeviceCodeAndTypeAndIsDeleted(deviceInfo.getDeviceCode(), finalSummary, 0);
        if (devicePriceDetail.isEmpty()) {
            deviceInfoRepository.save(deviceInfo);
            throw new BusinessException(BusinessExceptionCode.DEVICE_NOT_VALID,
                    messageCommon.getMessage(BusinessExceptionCode.DEVICE_NOT_VALID));
//            deviceInfo.setPrice(new BigDecimal(10000000));
        } else {

            deviceInfo.setPrice(devicePriceDetail.get().getPrice());
        }
        return DataUtil.convertObject(deviceInfoRepository.save(deviceInfo), x -> modelMapper.map(x, DeviceInfoDto.class));
    }

    @Override
    public List<DeviceCheckDto> getDeviceCheck(String transactionId) {
        return DataUtil.convertList(deviceCheckRepository.findAllItemCheck(transactionId), x -> modelMapper.map(x, DeviceCheckDto.class));
    }

    public String joinWithUnderscore(String... values) {
        return Arrays.stream(values)
                .filter(Objects::nonNull)
                .map(String::trim)
                .map(s -> s.replaceAll("\\s+", "_"))  // replace space → underscore
                .map(String::toUpperCase)
                .filter(s -> !s.isBlank())
                .collect(Collectors.joining("_"));
    }

    public List<String> splitImage(List<DeviceCheck> deviceChecks) {
        return deviceChecks.stream()
                .map(dc -> String.valueOf(dc.getValue()))
                .flatMap(v -> Arrays.stream(v.split(";")))
                .filter(s -> s != null && !s.isBlank()).collect(Collectors.toList());
    }

    public String findMaxType(String input, String input2, String input3) {
        int n1 = Integer.parseInt(input.replace("LOAI_", ""));
        int n2 = Integer.parseInt(input2.replace("LOAI_", ""));
        int n3 = Integer.parseInt(input3.replace("LOAI_", ""));
        return "LOAI_" + Math.max(Math.max(n1, n2), n3);
    }

    public static String maxType(List<String> values) {
        if (values == null || values.isEmpty()) return "LOAI_5";

        Integer bestNum = null;

        for (String s : values) {
            if (s == null) continue;

            Matcher m = TYPE_PATTERN.matcher(s.trim());
            if (!m.matches()) continue;

            int num = Integer.parseInt(m.group(1));
            if (bestNum == null || num > bestNum) {
                bestNum = num;
            }
        }

        return bestNum == null ? null : ("LOAI_" + bestNum);
    }


}
