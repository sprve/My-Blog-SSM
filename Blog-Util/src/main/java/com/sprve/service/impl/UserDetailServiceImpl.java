package com.sprve.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sprve.domain.entity.LoginUser;
import com.sprve.domain.entity.User;
import com.sprve.exception.SystemException;
import com.sprve.mapper.UserMapper;
import com.sprve.service.MenuService;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.sprve.domain.constants.SystemConstants.USER_ADMIN;
import static com.sprve.response.CodeEnum.LOGIN_ERROR;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private MenuService menuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUserName,username);
        User user =userMapper.selectOne(userLambdaQueryWrapper);
        if(ObjectUtil.isEmpty(user))
            throw new SystemException(LOGIN_ERROR);
        if(user.getType().equals(USER_ADMIN)){
            List<String> list = menuService.selectPermsByUserId(user.getId().toString());
            return new LoginUser(user,list);
        }
        return new LoginUser(user,null);
    }
}
