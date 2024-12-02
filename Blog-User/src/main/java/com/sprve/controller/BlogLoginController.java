package com.sprve.controller;

import com.sprve.aspect.SystemLog;
import com.sprve.domain.entity.User;
import com.sprve.domain.vo.BlogUserLoginVo;
import com.sprve.response.ResponseResult;
import com.sprve.service.BlogLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="UserApi",description = "userApi")
@RestController
public class BlogLoginController {

    @Resource
    private BlogLoginService blogLoginService;

    @Operation(summary = "登录接口",description = "登录")
    @SystemLog(businessName = "登录")
    @PostMapping("/login")
    public ResponseResult login(@Parameter(name = "User",description = "user") @RequestBody User user){
        BlogUserLoginVo data = blogLoginService.login(user);
        return ResponseResult.okResult(data);
    }

    @Operation(summary = "注销接口",description = "注销")
    @SystemLog(businessName = "注销")
    @PostMapping("/logout")
    public ResponseResult logout(){
        blogLoginService.logout();
        return ResponseResult.okResult();
    }
}