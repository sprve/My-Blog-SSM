package com.sprve.service.impl;

import com.sprve.Util.JwtUtil;
import com.sprve.domain.entity.LoginUser;
import com.sprve.domain.entity.User;
import com.sprve.service.BlogLoginService;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BlogUserServiceImpl implements BlogLoginService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Override
    public Object login(User user) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        if(Objects.isNull(authenticate))
            throw  new RuntimeException("用户名或密码错误");
        LoginUser loginUser =(LoginUser)authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();

        JwtUtil.createToken("userID",userId);

        return null;
    }
}
