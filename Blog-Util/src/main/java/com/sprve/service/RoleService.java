package com.sprve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sprve.domain.entity.Role;
import com.sprve.response.ResponseResult;

import java.util.List;

public interface RoleService extends IService<Role> {
    List<String> selectRolesByUserId(String userId);

    List<Role> selectRoleAll();

    List<Long> selectRoleIdByUserId(Long userId);

    void insertRole(Role role);

    ResponseResult selectRolePage(Role role, Integer pageNum, Integer pageSize);

    void updateRole(Role role);
}
