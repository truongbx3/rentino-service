package com.viettel.vss.controller;

import com.viettel.vss.base.BaseController;
import com.viettel.vss.dto.DeviceCheckRequest;
import com.viettel.vss.dto.DeviceInfoDto;
import com.viettel.vss.dto.ResponseDto;
import com.viettel.vss.service.DeviceInfoService;
import com.viettel.vss.service.DevicePriceService;
import com.viettel.vss.service.OpenAIService;
import com.viettel.vss.util.ResponseConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("devicePrice")
public class DevicePriceController extends BaseController {
    private DevicePriceService devicePriceService;
    private OpenAIService openAIService;


    public DevicePriceController(DevicePriceService devicePriceService) {
        super(devicePriceService);
        this.devicePriceService = devicePriceService;
    }


}
