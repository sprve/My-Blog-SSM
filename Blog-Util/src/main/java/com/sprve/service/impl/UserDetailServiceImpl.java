package com.sprve.service.impl;

import com.github.yulichang.query.MPJLambdaQueryWrapper;
import com.sprve.domain.entity.LoginUser;
import com.sprve.domain.entity.User;
import com.sprve.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MPJLambdaQueryWrapper<User> userMPJLambdaQueryWrapper = new MPJLambdaQueryWrapper<>();
        userMPJLambdaQueryWrapper.eq(User::getUserName,username);
        User user =userMapper.selectOne(userMPJLambdaQueryWrapper);
        if(Objects.isNull(user))
            throw new RuntimeException("用户不存在");
        LoginUser loginUser = new LoginUser();
        loginUser.setUser(user);
        return loginUser;
    }
}
