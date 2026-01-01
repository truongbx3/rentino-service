package com.viettel.vss.service.impl;

import com.viettel.vss.base.BaseServiceImpl;
import com.viettel.vss.constant.BusinessExceptionCode;
import com.viettel.vss.constant.ServiceMessageConstants;
import com.viettel.vss.dto.AttachFileDto;
import com.viettel.vss.dto.attach_file.AttachFileDTO;
import com.viettel.vss.dto.attach_file.AttachFileResponse;
import com.viettel.vss.dto.attach_file.AttachFileResponseDto;
import com.viettel.vss.dto.attach_file.SaveAttachFileRequest;
import com.viettel.vss.entity.AttachFile;
import com.viettel.vss.exception.BusinessException;
import com.viettel.vss.repository.AttachFileRepository;
import com.viettel.vss.service.AttachFileService;
import com.viettel.vss.service.MinioService;
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
public class AttachFileServiceImpl extends BaseServiceImpl<AttachFile, AttachFileDto> implements AttachFileService {

    @Value("${minio.bucketName.process}")
    private String bucketName;

    @Value("${spring.application.name}")
    private String appName;

    @Autowired
    MinioService minioService;

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MessageCommon messageCommon;
    private final AttachFileRepository attachFileRepository;

    public AttachFileServiceImpl(AttachFileRepository attachFileRepository) {
        super(attachFileRepository, AttachFile.class, AttachFileDto.class);
        this.attachFileRepository = attachFileRepository;
    }

    @Override
    @Transactional
    public List<AttachFileDto> createAttachFile(MultipartFile[] files) {
        List<MultipartFile> listFile = Arrays.asList(files);
        List<AttachFileDto> attachFiles = new ArrayList<>();
        listFile.forEach(file -> {
            int lastDotIndex = Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf('.');
            String tmpName = file.getOriginalFilename().substring(0, lastDotIndex) + "_" + new Date().getTime() + file.getOriginalFilename().substring(lastDotIndex);
            tmpName = tmpName.replace(" ", "_");
            minioService.uploadFile(file, bucketName, tmpName);
            AttachFile attachFile = new AttachFile();
            attachFile.setFileName(file.getOriginalFilename());
            attachFile.setPathName(tmpName);
            attachFile.setSystem(appName);
            attachFile = attachFileRepository.save(attachFile);
            attachFiles.add(modelMapper.map(attachFile, AttachFileDto.class));
        });
        return attachFiles;
    }

    @Override
    @Transactional
    public List<AttachFileResponse> save(List<SaveAttachFileRequest> requests) {
        List<String> tempFiles = new ArrayList<>();
        if (DataUtil.isNullOrEmpty(requests)) return Collections.emptyList();
        List<AttachFile> savedAttachments = new ArrayList<>();
        for (SaveAttachFileRequest request : requests) {
            var files = request.getFiles();
            if (files.length == 0) {
                return Collections.emptyList();
            }
            try {
                if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                    minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                }
            } catch (Exception exception) {
                log.error(messageCommon.getMessage(ServiceMessageConstants.MINIO_ERROR), exception);
                throw new BusinessException(ServiceMessageConstants.MINIO_ERROR,
                        messageCommon.getMessage(ServiceMessageConstants.MINIO_ERROR));
            }
            var attachFiles = setFiles(files, request);
            var savedAttachment = attachFileRepository.saveAll(attachFiles);
            savedAttachments.addAll(savedAttachment);
            tempFiles.addAll(attachFiles.stream().map(AttachFile::getFileName).collect(Collectors.toList()));
        }
        deleteFilesTemp(tempFiles);
        return savedAttachments.stream()
                .map(attachment -> {
                    var attachFileResponse = new AttachFileResponse();
                    attachFileResponse.setId(attachment.getId());
                    attachFileResponse.setFileName(attachment.getFileName());
                    attachFileResponse.setPathFile(attachment.getPathName());
                    return attachFileResponse;
                })
                .collect(Collectors.toList());
    }

    private void deleteFilesTemp(List<String> tempFiles) {
        if (DataUtil.isNullOrEmpty(tempFiles)) return;
        tempFiles.forEach(s -> {
            Path path = Paths.get(s);
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                log.info("Lỗi xoá files");
            }
        });
    }

    private List<AttachFile> setFiles(MultipartFile[] files, SaveAttachFileRequest request) {
        var attachFiles = new ArrayList<AttachFile>();
        Arrays.stream(files).filter(Objects::nonNull).forEach(file -> {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) return;

            var lastPointIndex = originalFilename.lastIndexOf('.');
            String objectName = DataUtil.safeToString(request.getBucketPath(), "other") + "/"
                    + DataUtil.safeToString(request.getUserName(), "anonymous") + "/"
                    + originalFilename.substring(0, lastPointIndex) + "-" + Instant.now().toEpochMilli()
                    + originalFilename.substring(lastPointIndex);
            try {
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build());
            } catch (Exception exception) {
                log.error(messageCommon.getMessage(ServiceMessageConstants.MINIO_ERROR), exception);
                throw new BusinessException(ServiceMessageConstants.MINIO_ERROR,
                        messageCommon.getMessage(ServiceMessageConstants.MINIO_ERROR));
            }

            var attachFile = new AttachFile();
            attachFile.setFileName(originalFilename);
            attachFile.setPathName(objectName);
            attachFiles.add(attachFile);
        });
        return attachFiles;
    }

    @Override
    public AttachFileDTO getFileById(Long id) {
        var attachment = attachFileRepository.findById(id)
                .orElseThrow(() -> new BusinessException(BusinessExceptionCode.ATTACH_FILE_NOT_FOUND,
                        messageCommon.getMessage(BusinessExceptionCode.ATTACH_FILE_NOT_FOUND, id)));
        try {
            var object = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(attachment.getPathName())
                    .build());

            var attachmentDto = new AttachFileDTO();
            attachmentDto.setFileName(attachment.getFileName());
            attachmentDto.setInputStream(new ByteArrayInputStream(object.readAllBytes()));

            return attachmentDto;
        } catch (Exception exception) {
            log.error(messageCommon.getMessage(ServiceMessageConstants.MINIO_ERROR), exception);
            throw new BusinessException(ServiceMessageConstants.MINIO_ERROR,
                    messageCommon.getMessage(ServiceMessageConstants.MINIO_ERROR));
        }
    }

    @Override
    public String getFileURLById(String id) {
        if (StringUtils.isNullOrEmpty(id)) {
            return "";
        }
        try {

            var attachment = attachFileRepository.findById(Long.parseLong(id));
            if (attachment.isEmpty()) {
                return "";
            }
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(attachment.get().getPathName())
                    .expiry(8, TimeUnit.HOURS)
                    .build());
            // .replaceAll(commonProperty.getMinioEndPoint(), commonProperty.getCrmGatewayIp());
        } catch (Exception exception) {
            log.error(messageCommon.getMessage(ServiceMessageConstants.MINIO_ERROR), exception);
            throw new BusinessException(ServiceMessageConstants.MINIO_ERROR,
                    messageCommon.getMessage(ServiceMessageConstants.MINIO_ERROR));
        }
    }

    @Override
    public List<String> createAttachFile(MultipartFile files, String nameBucket, Integer langType) {
        List<String> names = new ArrayList<>();
        String tmpName = files.getOriginalFilename();
        names.add(tmpName);
//        minioService.uploadFile(files, nameBucket, tmpName);
        return names;
    }


    @Override
    @Transactional
    public void delete(List<Long> ids) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        for (Long id : ids) {
            var attachment = attachFileRepository.findById(id)
                    .orElseThrow(() -> new BusinessException(BusinessExceptionCode.ATTACH_FILE_NOT_FOUND,
                            messageCommon.getMessage(BusinessExceptionCode.ATTACH_FILE_NOT_FOUND, id)));

            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(attachment.getPathName())
                    .build());
        }
        attachFileRepository.deleteAllById(ids);
    }

    @Override
    public AttachFileResponseDto getNameAndUrlById(Long id) {
        AttachFile entity = attachFileRepository.findById(id).orElseThrow(
                ()-> new BusinessException(BusinessExceptionCode.ATTACH_FILE_NOT_FOUND,
                        messageCommon.getMessage(BusinessExceptionCode.ATTACH_FILE_NOT_FOUND,id))
        );
        AttachFileResponseDto dto = modelMapper.map(entity,AttachFileResponseDto.class);
        dto.setUrl(getFileURLById(id.toString()));
        return dto;
    }

    @Override
    public AttachFileDTO getFileByName(String name) {
        var attachment = attachFileRepository.findByFileNameAndSystem(name,appName)
                .orElseThrow(() -> new BusinessException(BusinessExceptionCode.ATTACH_FILE_NOT_FOUND,
                        messageCommon.getMessage(BusinessExceptionCode.ATTACH_FILE_NOT_FOUND, name)));
        try {
            var object = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(attachment.getPathName())
                    .build());

            var attachmentDto = new AttachFileDTO();
            attachmentDto.setFileName(attachment.getFileName());
            attachmentDto.setInputStream(new ByteArrayInputStream(object.readAllBytes()));

            return attachmentDto;
        } catch (Exception exception) {
            log.error(messageCommon.getMessage(ServiceMessageConstants.MINIO_ERROR), exception);
            throw new BusinessException(ServiceMessageConstants.MINIO_ERROR,
                    messageCommon.getMessage(ServiceMessageConstants.MINIO_ERROR));
        }
    }

}
