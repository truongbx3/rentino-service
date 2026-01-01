package com.viettel.vss.controller;

import com.viettel.vss.base.BaseController;
import com.viettel.vss.dto.ActionDto;
import com.viettel.vss.dto.ResponseDto;
import com.viettel.vss.dto.action.ActionCreateRequest;
import com.viettel.vss.dto.action.ActionDetail;
import com.viettel.vss.dto.action.ActionListDto;
import com.viettel.vss.dto.action.FilterActionRequest;
import com.viettel.vss.service.ActionService;
import com.viettel.vss.util.ResponseConfig;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("action")
public class ActionController extends BaseController {
    private ActionService actionService;

    public ActionController(ActionService actionService){
        super(actionService);
        this.actionService = actionService;
    }

    @PostMapping("create")
    public ResponseEntity<ResponseDto<ActionDto>> createAction(HttpServletRequest httpServletRequest, @RequestBody ActionCreateRequest request){
        return ResponseConfig.success(actionService.saveObjectCus(request));
    }

    @PostMapping("update")
    public ResponseEntity<ResponseDto<ActionDto>> updateAction(HttpServletRequest httpServletRequest, @RequestBody ActionCreateRequest request){
        return ResponseConfig.success(actionService.saveObjectCus(request));
    }

    @GetMapping("detail")
    public ResponseEntity<ResponseDto<ActionDetail>> getActionDetail(HttpServletRequest httpServletRequest, @RequestParam Long id){
        return ResponseConfig.success(actionService.getDetailAction(id));
    }

    @PostMapping("filter")
    public ResponseEntity<ResponseDto<Page<ActionListDto>>> filterAction(HttpServletRequest httpServletRequest,
                                                                         @RequestBody FilterActionRequest request,
                                                                         @RequestParam(defaultValue = "20") Integer size,
                                                                         @RequestParam(defaultValue = "0") Integer page,
                                                                         @RequestParam(defaultValue = "updatedDate") String sortBy,
                                                                         @RequestParam(defaultValue = "DESC" )String sortDirection
                                                                         ){
        return ResponseConfig.success(actionService.filterAction(request, size, page, sortBy, sortDirection));
    }

}
