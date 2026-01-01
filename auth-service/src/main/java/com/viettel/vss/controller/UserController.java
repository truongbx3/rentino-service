package com.viettel.vss.controller;

import com.viettel.vss.base.BaseController;
import com.viettel.vss.dto.ResponseDto;
import com.viettel.vss.dto.UserDto;
import com.viettel.vss.service.UserService;
import com.viettel.vss.util.ResponseConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("users")
public class UserController extends BaseController {
	private UserService userService;

	public UserController(UserService userService){
		super(userService);
		this.userService = userService;
	}



    @PostMapping("create-user")
    public ResponseEntity<ResponseDto<UserDto>> createUser(@RequestBody UserDto userDto) {
        return ResponseConfig.success(userService.saveUser(userDto));
    }

    @PostMapping("update-user")
    public ResponseEntity<ResponseDto<String>> updateUser(HttpServletRequest httpServletRequest,
                                                           @RequestBody UserDto userDto) {
        userService.saveUser(userDto);
        return ResponseConfig.success("Success");
    }



    @Override
    @PostMapping("/deleteByIds")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ResponseDto<String>> deleteByIds(HttpServletRequest httpServletRequest, @RequestBody List<Long> ids) {
        userService.deleteByIds(ids);
        return ResponseConfig.success("Done");
    }
//    @PostMapping("recovery-password")
//    public ResponseEntity<ResponseDto<String>> recoveryPassword(HttpServletRequest httpServletRequest, @RequestParam Long id){
//        userService.recoveryPassword(id);
//        return ResponseConfig.success("Recovery password successfully");
//    }

}