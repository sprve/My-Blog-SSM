package com.sprve.controller;

import com.sprve.aspect.SystemLog;
import com.sprve.domain.entity.User;
import com.sprve.domain.vo.BlogUserLoginVo;
import com.sprve.response.ResponseResult;
import com.sprve.service.BlogLoginService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogLoginController {

    @Resource
    private BlogLoginService blogLoginService;

    @PostMapping("/login")
    @SystemLog(businessName = "登录")
    public ResponseResult login(@RequestBody User user){
        BlogUserLoginVo data = blogLoginService.login(user);
        return ResponseResult.okResult(data);
    }

    @SystemLog(businessName = "注销")
    @PostMapping("/logout")
    public ResponseResult logout(){
        blogLoginService.logout();
        return ResponseResult.okResult();
    }
}
