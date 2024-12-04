package com.sprve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sprve.domain.entity.RoleMenu;

public interface RoleMenuService extends IService<RoleMenu> {
    void deleteRoleMenuByRoleId(Long id);
}
