package com.sprve.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sprve.domain.entity.LoginUser;
import com.sprve.domain.entity.User;
import com.sprve.domain.vo.UserInfoVo;
import com.sprve.mapper.UserMapper;
import com.sprve.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public UserInfoVo userInfo() {
        LoginUser loginUser= (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId=loginUser.getUser().getId();
        User user = getById(userId);
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtil.copyProperties(user,userInfoVo);
        return userInfoVo;
    }
}
