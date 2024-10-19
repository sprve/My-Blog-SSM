package com.sprve.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sprve.domain.entity.LoginUser;
import com.sprve.domain.entity.User;
import com.sprve.domain.vo.UserInfoVo;
import com.sprve.exception.SystemException;
import com.sprve.mapper.UserMapper;
import com.sprve.response.CodeEnum;
import com.sprve.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public UserInfoVo userInfo() {
        LoginUser loginUser= (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId=loginUser.getUser().getId();
        User user = getById(userId);
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtil.copyProperties(user,userInfoVo);
        return userInfoVo;
    }

    @Override
    public void updateUserInfo(User user) {
      LoginUser loginUser  = (LoginUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      Long userId = loginUser.getUser().getId();
      LambdaQueryWrapper<User> userLambdaQueryWrapper =new LambdaQueryWrapper<>();
      userLambdaQueryWrapper.eq(User::getId,userId);
      update(user,userLambdaQueryWrapper);
    }

    @Override
    public void register(User user) {
        if(ObjectUtil.isEmpty(user.getUserName())){
            throw new SystemException(CodeEnum.USERNAME_NOT_NULL);
        }
        if(ObjectUtil.isEmpty(user.getPassword())){
            throw new SystemException(CodeEnum.PASSWORD_NOT_NULL);
        }
        if(ObjectUtil.isEmpty(user.getEmail())){
            throw new SystemException(CodeEnum.EMAIL_NOT_NULL);
        }
        if(ObjectUtil.isEmpty(user.getNickName())){
            throw new SystemException(CodeEnum.NICKNAME_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if(!userNameExist(user)){
            throw new SystemException(CodeEnum.USERNAME_EXIST);
        }
        //对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        //存入数据库
        save(user);
    }
    private boolean userNameExist(User user){
        LambdaQueryWrapper<User> userLambdaQueryWrapper =new LambdaQueryWrapper<>();
        userLambdaQueryWrapper
                .eq(User::getUserName,user.getUserName())
                .or()
                .eq(User::getNickName,user.getNickName());
        User originUser = getOne(userLambdaQueryWrapper);
        return ObjectUtil.isEmpty(originUser);
    }
}
