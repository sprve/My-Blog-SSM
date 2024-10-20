package com.sprve.controller;

import com.sprve.domain.entity.User;
import com.sprve.domain.vo.UserInfoVo;
import com.sprve.response.ResponseResult;
import com.sprve.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/userInfo")
    public ResponseResult updateUserInfo(@RequestBody User user){
        userService.updateUserInfo(user);
        return ResponseResult.okResult();
    }

    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user){
        userService.register(user);
        return  ResponseResult.okResult();
    }
}
