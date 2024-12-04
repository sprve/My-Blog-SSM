package com.sprve.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sprve.domain.entity.Link;
import com.sprve.domain.entity.LoginUser;
import com.sprve.domain.entity.User;
import com.sprve.domain.entity.UserRole;
import com.sprve.domain.vo.PageVo;
import com.sprve.domain.vo.UserInfoVo;
import com.sprve.domain.vo.UserVo;
import com.sprve.exception.SystemException;
import com.sprve.mapper.UserMapper;
import com.sprve.response.CodeEnum;
import com.sprve.response.ResponseResult;
import com.sprve.service.UserRoleService;
import com.sprve.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserRoleService userRoleService;

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

    @Override
    public ResponseResult selectUserPage(User user, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
        if(!ObjectUtil.isEmpty(user) && !ObjectUtil.isEmpty(user.getUserName()))
            queryWrapper.like(User::getUserName,user.getUserName());
        if(!ObjectUtil.isEmpty(user) && !ObjectUtil.isEmpty(user.getStatus()))
            queryWrapper.eq(User::getStatus,user.getStatus());
        if(!ObjectUtil.isEmpty(user) && !ObjectUtil.isEmpty(user.getPhonenumber()))
            queryWrapper.eq(User::getPhonenumber,user.getPhonenumber());

        Page<User> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);

        //转换成VO
        List<User> users = page.getRecords();
        List<UserVo> userVoList = users.stream()
                .map(u -> {
                    UserVo userVo = new UserVo();
                    BeanUtil.copyProperties(u, userVo);
                    return userVo;
                })
                .collect(Collectors.toList());
        PageVo pageVo = new PageVo(userVoList,page.getTotal());
        return ResponseResult.okResult(pageVo);
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

    @Override
    public boolean checkUserNameUnique(String userName) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getUserName,userName))==0;
    }

    @Override
    public boolean checkPhoneUnique(User user) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getPhonenumber,user.getPhonenumber()))==0;
    }

    @Override
    public boolean checkEmailUnique(User user) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getEmail,user.getEmail()))==0;
    }

    @Override
    public ResponseResult addUser(User user) {
        //密码加密处理
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        save(user);

        if(user.getRoleIds()!=null&&user.getRoleIds().length>0){
            insertUserRole(user);
        }
        return ResponseResult.okResult();
    }

    @Override
    public void updateUser(User user) {
        LambdaQueryWrapper<UserRole> userRoleUpdateWrapper = new LambdaQueryWrapper<>();
        userRoleUpdateWrapper.eq(UserRole::getUserId,user.getId());
        userRoleService.remove(userRoleUpdateWrapper);

        // 新增用户与角色管理
        insertUserRole(user);
        // 更新用户信息
        updateById(user);
    }

    private void insertUserRole(User user) {
        List<UserRole> sysUserRoles = Arrays.stream(user.getRoleIds())
                .map(roleId -> new UserRole(user.getId(), roleId)).collect(Collectors.toList());
        userRoleService.saveBatch(sysUserRoles);
    }
}
