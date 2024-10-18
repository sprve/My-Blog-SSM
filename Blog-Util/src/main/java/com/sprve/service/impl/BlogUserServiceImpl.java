package com.sprve.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.sprve.Util.JwtUtil;
import com.sprve.Util.RedisUtil;
import com.sprve.domain.entity.LoginUser;
import com.sprve.domain.entity.User;
import com.sprve.domain.vo.BlogUserLoginVo;
import com.sprve.domain.vo.UserInfoVo;
import com.sprve.exception.SystemException;
import com.sprve.service.BlogLoginService;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static com.sprve.response.CodeEnum.LOGIN_ERROR;

@Service
public class BlogUserServiceImpl implements BlogLoginService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public BlogUserLoginVo login(User user) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        if(ObjectUtil.isEmpty(authenticate))
            throw  new SystemException(LOGIN_ERROR);
        LoginUser loginUser =(LoginUser)authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();

        String token = JwtUtil.createToken("userId",userId);
        redisUtil.setCacheObject("bloglogin"+userId,loginUser);
        redisUtil.expire("bloglogin"+userId,600);

        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtil.copyProperties(loginUser.getUser(),userInfoVo);

        BlogUserLoginVo blogUserLoginVo = new BlogUserLoginVo(token,userInfoVo);

        return blogUserLoginVo;
    }

    @Override
    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        long userId = loginUser.getUser().getId();
        redisUtil.deleteObject("bloglogin" + userId);
    }
}
