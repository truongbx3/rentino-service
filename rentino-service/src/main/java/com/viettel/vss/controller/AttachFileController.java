package com.viettel.vss.controller;

import com.viettel.vss.base.BaseController;
import com.viettel.vss.dto.AttachFileDto;
import com.viettel.vss.dto.ResponseDto;
import com.viettel.vss.dto.attach_file.AttachFileResponse;
import com.viettel.vss.dto.attach_file.AttachFileResponseDto;
import com.viettel.vss.dto.attach_file.SaveAttachFileRequest;
import com.viettel.vss.service.AttachFileService;
import com.viettel.vss.util.ResponseConfig;
//import io.minio.errors.*;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.XmlParserException;
import org.apache.commons.math3.exception.InsufficientDataException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.rmi.ServerException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("attach-file")
public class AttachFileController extends BaseController {
    private AttachFileService attachFileService;
	public AttachFileController(AttachFileService attachFileService){
		super(attachFileService);
		this.attachFileService = attachFileService;
	}

    @PostMapping(value="create")
	public  ResponseEntity<ResponseDto<List<AttachFileDto>>> createAttachFileMapping(HttpServletRequest httpServletRequest, @RequestParam("files") MultipartFile[] files){
		return ResponseConfig.success(attachFileService.createAttachFile(files));
	}

	@GetMapping("/{id}/data")
	public ResponseEntity<InputStreamResource> download(@PathVariable Long id) {
		var attachmentDTO = attachFileService.getFileById(id);
		return ResponseEntity.ok()
				// Content-Disposition
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + attachmentDTO.getFileName())
				.header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
				// Content-Type
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(attachmentDTO.getInputStream()));
	}


	@GetMapping("/{id}/presignedUrl")
	public ResponseEntity<ResponseDto<String>> getURL(@PathVariable String id) {
		var url = attachFileService.getFileURLById(id);
		return ResponseConfig.success(url);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseDto<Long>> delete(@PathVariable Long id) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, io.minio.errors.ServerException, io.minio.errors.InsufficientDataException {
		List<Long> ids = new ArrayList<>();
		ids.add(id);
		attachFileService.delete(ids);
		return  ResponseConfig.success(id);
	}


	@DeleteMapping("/deleteIds")
	public ResponseEntity<ResponseDto<List<Long>>> delete(@RequestParam List<Long> ids) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, io.minio.errors.ServerException, io.minio.errors.InsufficientDataException {
		attachFileService.delete(ids);
		return ResponseConfig.success(ids);
	}

	@PostMapping("/save-list-attach-files")
	public ResponseEntity<ResponseDto<List<AttachFileResponse>>> save(@RequestBody List<SaveAttachFileRequest> saveAttachFileRequests) {
		var response = attachFileService.save(saveAttachFileRequests);
		return  ResponseConfig.success(response);
	}

		@PostMapping("/download-file-by-id/{id}")
		public ResponseEntity<InputStreamResource> downloadFileById(@PathVariable Long id) {
			var attachmentDTO = attachFileService.getFileById(id);
			return ResponseEntity.ok()
					// Content-Disposition
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + attachmentDTO.getFileName())
					.header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
					// Content-Type
					.contentType(MediaType.APPLICATION_OCTET_STREAM)
					.body(new InputStreamResource(attachmentDTO.getInputStream()));
		}

	@GetMapping(value = "get-name-and-url")
	public ResponseEntity<ResponseDto<AttachFileResponseDto>> getNameAndUrlById(HttpServletRequest httpServletRequest,
																				@RequestParam Long id){
		return ResponseConfig.success(attachFileService.getNameAndUrlById(id));
	}
}
