package com.viettel.vss.service;

import com.viettel.vss.base.BaseService;
import com.viettel.vss.dto.AttachFileDto;
import com.viettel.vss.dto.attach_file.AttachFileDTO;
import com.viettel.vss.dto.attach_file.AttachFileResponse;
import com.viettel.vss.dto.attach_file.AttachFileResponseDto;
import com.viettel.vss.dto.attach_file.SaveAttachFileRequest;
import com.viettel.vss.entity.AttachFile;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface AttachFileService extends BaseService<AttachFile, AttachFileDto>{
    List<AttachFileDto> createAttachFile(MultipartFile[] files);
    List<AttachFileResponse> save(List<SaveAttachFileRequest> requests);

    AttachFileDTO getFileById(Long id);

    String getFileURLById(String id);

    List<String> createAttachFile(MultipartFile files, String nameBucket, Integer langType);

    void delete(List<Long> ids) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    AttachFileResponseDto getNameAndUrlById(Long id);

    AttachFileDTO getFileByName(String name);
}
