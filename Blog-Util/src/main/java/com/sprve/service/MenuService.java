package com.sprve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sprve.domain.entity.Menu;
import com.sprve.domain.vo.MenuTreeVo;

import java.util.List;

public interface MenuService extends IService<Menu> {
    List<String> selectPermsByUserId(String userId);

    List<Menu> selectRouterMenuTree(String userId);

    List<Menu> selectMenuList(Menu menu);

    boolean hasChild(Long menuId);

    List<Long> selectMenuListByRoleId(Long roleId);

    List<MenuTreeVo> buildMenuSelectTree(List<Menu> menus);
}
