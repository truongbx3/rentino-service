package com.viettel.vss.controller;

import com.viettel.vss.base.BaseController;
import com.viettel.vss.dto.DeviceCheckRequest;
import com.viettel.vss.dto.DeviceInfoDto;
import com.viettel.vss.dto.DeviceQuestionDto;
import com.viettel.vss.dto.ResponseDto;
import com.viettel.vss.service.DeviceInfoService;
import com.viettel.vss.service.DeviceQuestionService;
import com.viettel.vss.service.OpenAIService;
import com.viettel.vss.util.ResponseConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("deviceQuestion")
public class DeviceQuestionController extends BaseController {
    private DeviceQuestionService deviceQuestionService;
    private OpenAIService openAIService;


    public DeviceQuestionController(DeviceQuestionService deviceQuestionService) {
        super(deviceQuestionService);
        this.deviceQuestionService = deviceQuestionService;
    }

    @GetMapping(value = "getQuestion")
    public ResponseEntity<ResponseDto<List<DeviceQuestionDto>>> getQuestion(Principal principal , @RequestParam String type) {

        return ResponseConfig.success(deviceQuestionService.findAllQuestionByType(type));
    }


}
