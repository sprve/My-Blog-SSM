package com.sprve.controller;

import com.sprve.domain.entity.User;
import com.sprve.response.ResponseResult;
import com.sprve.service.LoginService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    @Resource
    private LoginService loginService;

    @PostMapping("/user/login")
    public ResponseResult login(@Parameter(name = "User", description = "user") @RequestBody User user) {
        Map<String,String> data = new HashMap<>();
        data.put("token",loginService.login(user));
        return ResponseResult.okResult(data);
    }

    @PostMapping("/logout")
    public ResponseResult logout() {
        loginService.logout();
        return ResponseResult.okResult();
    }
}
