package com.sprve.controller;

import com.sprve.domain.vo.UserInfoVo;
import com.sprve.response.ResponseResult;
import com.sprve.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;

    @GetMapping("/userInfo")
    public ResponseResult userInfo(){
        UserInfoVo data =userService.userInfo();
        return ResponseResult.okResult(data);
    }
}
