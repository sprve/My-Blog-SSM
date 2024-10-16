package com.sprve.controller;

import com.sprve.domain.entity.User;
import com.sprve.domain.vo.BlogUserLoginVo;
import com.sprve.response.ResponseResult;
import com.sprve.service.BlogLoginService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class BlogLoginController {

    @Resource
    private BlogLoginService blogLoginService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        BlogUserLoginVo data = blogLoginService.login(user);
        return ResponseResult.okResult(data);
    }

    @PostMapping("/logout")
    public ResponseResult logout(){
        blogLoginService.logout();
        return ResponseResult.okResult();
    }
}
