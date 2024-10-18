package com.sprve.exception;

import com.alibaba.fastjson2.JSON;
import com.sprve.response.CodeEnum;
import com.sprve.response.ResponseResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ResponseResult result = null;
        if(authException instanceof BadCredentialsException)
            result =ResponseResult.errorResult(CodeEnum.LOGIN_ERROR.getCode(),authException.getMessage());
        else if(authException instanceof InsufficientAuthenticationException)
            result =ResponseResult.errorResult(CodeEnum.NEED_LOGIN);
        else
            result = ResponseResult.errorResult(CodeEnum.SYSTEM_ERROR,"认证或授权失败");
        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().print(JSON.toJSONString(result));
    }
}
