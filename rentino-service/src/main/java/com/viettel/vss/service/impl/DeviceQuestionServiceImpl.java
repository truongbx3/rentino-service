package com.viettel.vss.service.impl;

import com.google.common.collect.Lists;
import com.viettel.vss.base.BaseServiceImpl;
import com.viettel.vss.constant.BusinessExceptionCode;
import com.viettel.vss.constant.ServiceMessageConstants;
import com.viettel.vss.dto.AttachFileDto;
import com.viettel.vss.dto.DeviceQuestionAnswerDto;
import com.viettel.vss.dto.DeviceQuestionDto;
import com.viettel.vss.dto.attach_file.AttachFileDTO;
import com.viettel.vss.dto.attach_file.AttachFileResponse;
import com.viettel.vss.dto.attach_file.AttachFileResponseDto;
import com.viettel.vss.dto.attach_file.SaveAttachFileRequest;
import com.viettel.vss.entity.AttachFile;
import com.viettel.vss.entity.DeviceQuestion;
import com.viettel.vss.exception.BusinessException;
import com.viettel.vss.repository.AttachFileRepository;
import com.viettel.vss.repository.DeviceQuestionRepository;
import com.viettel.vss.service.AttachFileService;
import com.viettel.vss.service.DeviceQuestionService;
import com.viettel.vss.service.MinioService;
import com.viettel.vss.util.CommonProperty;
import com.viettel.vss.util.DataUtil;
import com.viettel.vss.util.MessageCommon;
import com.viettel.vss.util.StringUtils;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DeviceQuestionServiceImpl extends BaseServiceImpl<DeviceQuestion, DeviceQuestionDto> implements DeviceQuestionService {


    @Autowired
    private MessageCommon messageCommon;
    private final DeviceQuestionRepository deviceQuestionRepository;

    public DeviceQuestionServiceImpl(DeviceQuestionRepository deviceQuestionRepository) {
        super(deviceQuestionRepository, DeviceQuestion.class, DeviceQuestionDto.class);
        this.deviceQuestionRepository = deviceQuestionRepository;
    }

    @Override
    public List<DeviceQuestionDto> findAllQuestionByType(String type) {
        List<DeviceQuestion> deviceQuestions = deviceQuestionRepository.findAllByTypeAndIsDeletedOrderByOrderDesc(type, 0);
        return deviceQuestions.stream()
                .map(q -> {
                    DeviceQuestionDto dto = modelMapper.map(q, DeviceQuestionDto.class);
                    dto.setDeviceQuestionAnswerDtos(
                            q.getAnswers().stream()
                                    .map(a -> modelMapper.map(a, DeviceQuestionAnswerDto.class)).collect(Collectors.toList())
                    );
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
