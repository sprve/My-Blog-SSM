package com.sprve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sprve.domain.entity.Role;

import java.util.List;

public interface RoleService extends IService<Role> {
    List<String> selectRolesByUserId(String userId);
}
