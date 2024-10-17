package com.sprve.fliter;

import cn.hutool.json.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.sprve.Util.JwtUtil;
import com.sprve.Util.RedisUtil;
import com.sprve.domain.entity.LoginUser;
import com.sprve.response.CodeEnum;
import com.sprve.response.ResponseResult;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    private RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");
        JSONObject jsonObject = null;
        if(Objects.isNull(token)){
            filterChain.doFilter(request,response);
            return;
        }
        if(!JwtUtil.validate(token)) {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            ResponseResult result = ResponseResult.errorResult(CodeEnum.NEED_LOGIN);
            response.getWriter().print(JSON.toJSONString(result));
            return;
        }
        jsonObject = JwtUtil.getJSONObject(token);
        String userId=jsonObject.getStr("userId");

        LoginUser loginUser = redisUtil.getCacheObject("bloglogin"+userId);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser,null,null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request,response);
    }
}
