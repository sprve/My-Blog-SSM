package com.sprve.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.sprve.domain.entity.*;
import com.sprve.mapper.MenuMapper;
import com.sprve.mapper.UserMapper;
import com.sprve.mapper.UserRoleMapper;
import com.sprve.service.MenuService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.sprve.domain.constants.SystemConstants.*;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Resource
    UserRoleMapper userRoleMapper;

    @Resource
    UserMapper userMapper;

    @Override
    public List<String> selectPermsByUserId(String userId) {
        List<String> perms;
        if(userId.equals("1")) {
            LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
            menuLambdaQueryWrapper
                    .select(Menu::getPerms)
                    .ne(Menu::getMenuType,MENU_MULU);
            perms = listObjs(menuLambdaQueryWrapper);
        }
        else {
            MPJLambdaWrapper<UserRole> userRoleMPJLambdaWrapper = new MPJLambdaWrapper<>();
            userRoleMPJLambdaWrapper
                    .select(Menu::getPerms)
                    .innerJoin(RoleMenu.class, RoleMenu::getRoleId, UserRole::getRoleId)
                    .innerJoin(Role.class,Role::getId,UserRole::getRoleId)
                    .innerJoin(Menu.class, Menu::getId, RoleMenu::getMenuId)
                    .eq(UserRole::getUserId, userId)
                    .eq(Role::getStatus, ROLE_STATUS_ENABLE)
                    .eq(Menu::getStatus, MENU_STATUS_ENABLE)
                    .ne(Menu::getMenuType, MENU_MULU);
            perms = userRoleMapper.selectJoinList(java.lang.String.class, userRoleMPJLambdaWrapper);
        }
        perms=perms
                .stream()
                .distinct()
                .collect(Collectors.toList());
        return perms;
    }

    @Override
    public List<Menu> selectRouterMenuTree(String userId) {
        List<Menu> menuList;
        if(userId.equals("1")) {
            LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
            menuLambdaQueryWrapper
                    .ne(Menu::getMenuType,MENU_ANNUI)
                    .orderByDesc(Menu::getParentId)
                    .orderByDesc(Menu::getOrderNum);
            menuList = list(menuLambdaQueryWrapper);
        }
        else {
            MPJLambdaWrapper<UserRole> userRoleMPJLambdaWrapper = new MPJLambdaWrapper<>();
            userRoleMPJLambdaWrapper
                    .selectAll(Menu.class)
                    .innerJoin(RoleMenu.class, RoleMenu::getRoleId, UserRole::getRoleId)
                    .innerJoin(Role.class,Role::getId,UserRole::getRoleId)
                    .innerJoin(Menu.class, Menu::getId, RoleMenu::getMenuId)
                    .eq(UserRole::getUserId, userId)
                    .eq(Role::getStatus, ROLE_STATUS_ENABLE)
                    .eq(Menu::getStatus, MENU_STATUS_ENABLE)
                    .ne(Menu::getMenuType, MENU_ANNUI)
                    .orderByDesc(Menu::getParentId)
                    .orderByDesc(Menu::getOrderNum);
            menuList = userRoleMapper.selectJoinList(Menu.class, userRoleMPJLambdaWrapper);
        }
        menuList=menuList
                .stream()
                .map(menu -> {
                    if(ObjectUtil.isEmpty(menu.getPerms())){
                        menu.setPerms("");
                    }
                    return menu;
                })
                .distinct()
                .collect(Collectors.toList());
        menuList = builderMenuTree(menuList,"0");

        return menuList;
    }

    private List<Menu> builderMenuTree(List<Menu> menus,String root) {
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().toString().equals(root))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }

    /**
     * 获取存入参数的 子Menu集合
     * @param menu
     * @param menus
     * @return
     */
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().toString().equals(menu.getId().toString()))
                .map(m->m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
        return childrenList;
    }
}
