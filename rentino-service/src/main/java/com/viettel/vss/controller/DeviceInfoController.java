package com.viettel.vss.controller;

import com.viettel.vss.base.BaseController;
import com.viettel.vss.dto.*;
import com.viettel.vss.service.DeviceInfoService;
import com.viettel.vss.service.OpenAIService;
import com.viettel.vss.util.ResponseConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("deviceInfo")
public class DeviceInfoController extends BaseController {
    private DeviceInfoService deviceInfoService;
    private OpenAIService openAIService;


    public DeviceInfoController(DeviceInfoService deviceInfoService,OpenAIService openAIService) {
        super(deviceInfoService);
        this.deviceInfoService = deviceInfoService;
        this.openAIService = openAIService;
    }

    @PostMapping(value = "save")
    public ResponseEntity<ResponseDto<String>> saveDeviceInfo(Principal principal , @RequestBody DeviceInfoDto deviceInfoDtos) {
        deviceInfoService.saveDeviceInfo(principal.getName(),deviceInfoDtos);
        return ResponseConfig.success("Success");
    }

    @GetMapping(value = "getTransaction")
    public ResponseEntity<ResponseDto<String>> getTransaction(Principal principal) {
        return ResponseConfig.success(deviceInfoService.generateUUid(principal.getName()));
    }

    @PostMapping(value = "saveCheckInfo")
    public ResponseEntity<ResponseDto<String>> saveCheckInfo(Principal principal,@RequestBody DeviceCheckRequest deviceCheckRequest) {
        deviceInfoService.deviceCheck(deviceCheckRequest,principal.getName());
        return ResponseConfig.success("Success");
    }


    @GetMapping(value = "getCheckInfo")
    public ResponseEntity<ResponseDto<List<DeviceCheckDto>>> getCheckInfo(Principal principal, @RequestParam String transactionId) {
        return ResponseConfig.success(deviceInfoService.getDeviceCheck(transactionId));
    }



    @PostMapping(value = "checkDevice")
    public ResponseEntity<ResponseDto<DeviceInfoDto>> checkDevice(Principal principal, @RequestParam String transaction) throws Exception {
//        System.out.println(openAIService.analyze("http://72.61.120.252:9000/rentino/mt_camera_1764408574231.png","http://72.61.120.252:9000/rentino/ms_camera_1764408573764.png","http://72.61.120.252:9000/rentino/tiêu_chí_1764390909884.png"));
        return ResponseConfig.success(deviceInfoService.analyze(principal.getName(),transaction));
    }


}
