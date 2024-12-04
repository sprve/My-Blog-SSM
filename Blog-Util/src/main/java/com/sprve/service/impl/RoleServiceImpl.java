package com.sprve.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.sprve.domain.entity.Role;
import com.sprve.domain.entity.UserRole;
import com.sprve.mapper.RoleMapper;
import com.sprve.mapper.UserRoleMapper;
import com.sprve.service.RoleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public List<String> selectRolesByUserId(String userId) {
        List<String> roles = new ArrayList<>();
        if(userId.equals("1")){
            roles.add("admin");
        }
        else{
            MPJLambdaWrapper<UserRole> userRoleMPJLambdaWrapper =new MPJLambdaWrapper<>();
            userRoleMPJLambdaWrapper
                    .select(Role::getRoleKey)
                    .innerJoin(Role.class,Role::getId,UserRole::getRoleId)
                    .eq(UserRole::getUserId,userId);
            roles = userRoleMapper.selectJoinList(java.lang.String.class,userRoleMPJLambdaWrapper);
        }
        roles = roles
                .stream()
                .distinct()
                .collect(Collectors.toList());
        return roles;
    }
}
