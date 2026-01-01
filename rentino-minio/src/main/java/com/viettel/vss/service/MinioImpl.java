package com.viettel.vss.service;

import com.viettel.vss.exception.MinioException;
import io.minio.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;

/**
 * @author truongbx7
 * @project vss-fw
 * @date 6/13/2023
 */
@Slf4j
@Component
public class MinioImpl implements MinioService {
    @Autowired
    private MinioClient minioClient;
    public static final String MINIO_ERROR = "MINIO_ERROR";

    @Override
    public String getPresignedUrl(String fileName, String bucketName){
        try {
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(fileName)
                    .build());
        } catch (Exception exception) {
            throw new MinioException(MINIO_ERROR,  "getPresignedUrl error fileName = " + fileName + " , bucketName = " + bucketName, exception);
        }
    }

    @Override
    public void deleteFile(String fileName, String bucketName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build());
        } catch (Exception exception) {
            throw new MinioException(MINIO_ERROR,  "deleteFile error fileName = " + fileName + " , bucketName = " + bucketName, exception);
        }
    }

    @Override
    public ByteArrayInputStream getInputStreamTemplate(String fileName, String bucketName) {
        try {
            var object = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build());
            return new ByteArrayInputStream(object.readAllBytes());
        } catch (Exception exception) {
            throw new MinioException(MINIO_ERROR,  "getInputStreamTemplate error fileName = " + fileName + " , bucketName = " + bucketName, exception);
        }
    }

    @Override
    public boolean bucketExists(String bucketName) {
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception exception) {
            throw new MinioException(MINIO_ERROR,  "bucketExists error  bucketName = " + bucketName, exception);
        }
    }

    @Override
    public void makeBucket(String bucketName) {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        } catch (Exception exception) {
            throw new MinioException(MINIO_ERROR,  "makeBucket error  bucketName = " + bucketName, exception);
        }
    }

    @Override
    public ByteArrayInputStream getFile(String fileName, String bucketName) {

        try {
            if (bucketExists(bucketName)) {
                var object = minioClient.getObject(GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .build());
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(object.readAllBytes());
                return byteArrayInputStream;
            } else {
                return null;
            }
        } catch (Exception exception) {
            throw new MinioException(MINIO_ERROR,  "getFile error fileName = " + fileName + " , bucketName = " + bucketName, exception);
        }
    }

    @Override
    public String uploadFile(MultipartFile file, String bucketName, String tmpName) {
        if (!bucketExists(bucketName)) {
            makeBucket(bucketName);
        }
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(tmpName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
            return "Done";
        } catch (Exception exception) {
            throw new MinioException(MINIO_ERROR,  "uploadFile error bucketName = " + bucketName, exception);
        }
    }


}
