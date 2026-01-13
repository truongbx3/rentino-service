package com.viettel.vss.controller;

import com.viettel.vss.dto.BanksDto;
import com.viettel.vss.dto.ResponseDto;
import com.viettel.vss.dto.attach_file.AttachFileDTO;
import com.viettel.vss.service.BanksService;
import com.viettel.vss.service.ContractService;
import com.viettel.vss.util.ResponseConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("bank")
public class BankController {
    @Autowired
    BanksService banksService;

    @GetMapping("/getBanks")
    public ResponseEntity<ResponseDto<List<BanksDto>>> getBanks(Principal principal) throws Exception {
        return ResponseConfig.success(banksService.getBanksList());
    }

}
