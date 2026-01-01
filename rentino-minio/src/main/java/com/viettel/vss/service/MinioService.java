package com.viettel.vss.service;

import java.io.ByteArrayInputStream;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author truongbx7
 * @project vss-fw
 * @date 6/13/2023
 */
public interface MinioService {
    String getPresignedUrl(String fileName, String bucketName);
    void deleteFile(String fileName, String bucketName);
    ByteArrayInputStream getInputStreamTemplate(String fileName, String bucketName);
    boolean bucketExists(String bucketName);
    void makeBucket(String bucketName);
    ByteArrayInputStream getFile(String fileName, String bucketName);
    String uploadFile(MultipartFile file, String bucketName, String tmpName);
}
