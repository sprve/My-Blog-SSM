package com.sprve.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.sprve.domain.entity.*;
import com.sprve.domain.vo.MenuTreeVo;
import com.sprve.mapper.MenuMapper;
import com.sprve.mapper.UserMapper;
import com.sprve.mapper.UserRoleMapper;
import com.sprve.service.MenuService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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

    @Override
    public List<Menu> selectMenuList(Menu menu) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        //menuName模糊查询
        if(!ObjectUtil.isEmpty(menu) && !ObjectUtil.isEmpty(menu.getMenuName()))
            queryWrapper.like(Menu::getMenuName,menu.getMenuName());
        if(!ObjectUtil.isEmpty(menu) && !ObjectUtil.isEmpty(menu.getStatus()))
            queryWrapper.eq(Menu::getStatus,menu.getStatus());
        //排序 parent_id和order_num
        queryWrapper.orderByAsc(Menu::getParentId,Menu::getOrderNum);
        List<Menu> menus = list(queryWrapper);;
        return menus;

    }

    @Override
    public boolean hasChild(Long menuId) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId,menuId);
        return count(queryWrapper) != 0;
    }

    @Override
    public List<Long> selectMenuListByRoleId(Long roleId) {
        MPJLambdaWrapper<Menu> menuMPJLambdaWrapper = new MPJLambdaWrapper<>();
        menuMPJLambdaWrapper
                .select(Menu::getId)
                .innerJoin(RoleMenu.class,RoleMenu::getMenuId,Menu::getId)
                .eq(RoleMenu::getRoleId,roleId)
                .orderByDesc(Menu::getParentId)
                .orderByDesc(Menu::getOrderNum);
        List<Long> dataList = getBaseMapper().selectJoinList(java.lang.Long.class,menuMPJLambdaWrapper);
        return dataList;
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

    public  List<MenuTreeVo> buildMenuSelectTree(List<Menu> menus) {
        List<MenuTreeVo> MenuTreeVos = menus.stream()
                .map(m -> new MenuTreeVo(m.getId(), m.getMenuName(), m.getParentId(), null))
                .collect(Collectors.toList());
        List<MenuTreeVo> options = MenuTreeVos.stream()
                .filter(o -> o.getParentId().equals(0L))
                .map(o -> o.setChildren(getChildList(MenuTreeVos, o)))
                .collect(Collectors.toList());


        return options;
    }


    /**
     * 得到子节点列表
     */
    private List<MenuTreeVo> getChildList(List<MenuTreeVo> list, MenuTreeVo option) {
        List<MenuTreeVo> options = list.stream()
                .filter(o -> Objects.equals(o.getParentId(), option.getId()))
                .map(o -> o.setChildren(getChildList(list, o)))
                .collect(Collectors.toList());
        return options;

    }
}
