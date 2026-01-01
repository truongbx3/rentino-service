package com.viettel.vss.controller;

import com.viettel.vss.dto.attach_file.AttachFileDTO;
import com.viettel.vss.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("contract")
public class ContractController {
    @Autowired
    ContractService contractService;

    @GetMapping("/download-contract")
    public ResponseEntity<InputStreamResource> downloadFileById(Principal principal, @RequestParam String transactionId) throws Exception {
        AttachFileDTO attachmentDTO = contractService.createContract(principal.getName(),transactionId);
        return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + attachmentDTO.getFileName())
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                // Content-Type
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(attachmentDTO.getInputStream()));
    }

}
