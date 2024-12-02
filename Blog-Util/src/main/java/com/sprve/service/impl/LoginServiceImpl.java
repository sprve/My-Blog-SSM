package com.sprve.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.sprve.Util.JwtUtil;
import com.sprve.Util.RedisUtil;
import com.sprve.domain.entity.LoginUser;
import com.sprve.domain.entity.User;
import com.sprve.domain.vo.BlogUserLoginVo;
import com.sprve.exception.SystemException;
import com.sprve.service.LoginService;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;

import static com.sprve.response.CodeEnum.LOGIN_ERROR;

@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    AuthenticationManager authenticationManager;

    @Resource
    RedisUtil redisUtil;

    @Override
    public String login(User user) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        if(ObjectUtil.isEmpty(authenticate))
            throw  new SystemException(LOGIN_ERROR);
        LoginUser loginUser = (LoginUser)authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String token = JwtUtil.createToken("userId",userId);
        redisUtil.setCacheObject("adminlogin"+userId,loginUser);
        redisUtil.expire("adminlogin"+userId,600);
        return token;
    }

    @Override
    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser)authentication.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        redisUtil.deleteObject("adminlogin"+userId);
    }
}
