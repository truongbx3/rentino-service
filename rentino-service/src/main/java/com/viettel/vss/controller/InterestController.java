package com.viettel.vss.controller;

import com.viettel.vss.base.BaseController;
import com.viettel.vss.dto.BorrowDto;
import com.viettel.vss.dto.InterestDto;
import com.viettel.vss.dto.ResponseDto;
import com.viettel.vss.dto.UserBorrowDto;
import com.viettel.vss.dto.attach_file.AttachFileDTO;
import com.viettel.vss.service.InterestService;
import com.viettel.vss.service.OpenAIService;
import com.viettel.vss.util.ResponseConfig;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("interest")
public class InterestController extends BaseController {
    private InterestService interestService;
    private OpenAIService openAIService;


    public InterestController(InterestService interestService) {
        super(interestService);
        this.interestService = interestService;
    }

    @GetMapping(value = "getInterest")
    public ResponseEntity<ResponseDto<List<InterestDto>>> getFixedInterest(Principal principal, @RequestParam String type, @RequestParam(required = false) Integer isFixed) {

        return ResponseConfig.success(interestService.getInterest(type, isFixed));
    }


    @PostMapping(value = "getEstimateCost")
    public ResponseEntity<ResponseDto<List<InterestDto>>> getEstimateCost(Principal principal, @RequestBody @Valid BorrowDto borrowDto) {

        return ResponseConfig.success(interestService.getEstimateCode(borrowDto));
    }

    @PostMapping(value = "borrow")
    public ResponseEntity<ResponseDto<UserBorrowDto>> borrow(Principal principal, @RequestBody @Valid BorrowDto borrowDto) {

        return ResponseConfig.success(interestService.saveBorrowInfo(principal.getName(), borrowDto));
    }



}
