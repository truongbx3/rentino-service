package com.viettel.vss.base;


import com.viettel.vss.dto.RequestDto;
import com.viettel.vss.dto.ResponseDto;
import com.viettel.vss.util.ResponseConfig;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public class BaseController {
    public final BaseService baseService;
    protected   Logger LOG = LogManager.getLogger(this.getClass());
    public BaseController(BaseService baseService) {
        this.baseService = baseService;
    }

    @ApiOperation(value = "delete data by list ids ")
    @PreAuthorize("autoRole(#httpServletRequest,'DELETE')")
    @PostMapping("/deleteByIds")
    public ResponseEntity deleteByIds(HttpServletRequest httpServletRequest,@RequestBody List<Long> ids) {
        baseService.deleteByIds(ids);
        return new ResponseEntity(null, HttpStatus.OK);
    }

    @ApiOperation(value = "get data by list ids ")
    @GetMapping("/findByIds")
    public <T> ResponseEntity<ResponseDto<List<T>>> findByIds(HttpServletRequest httpServletRequest, @RequestParam List<Long> ids) {
        return ResponseConfig.success(baseService.findByIds(ids));
    }

    @ApiOperation(value = "get data by list condition ")
    @PostMapping("/search")
    public <T> ResponseEntity<ResponseDto<Page<T>>> find(HttpServletRequest httpServletRequest,@RequestBody RequestDto requestDto) {
        return ResponseConfig.success(baseService.findAll(requestDto));
    }

}
