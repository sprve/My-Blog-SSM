package com.sprve.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.sprve.domain.entity.Role;
import com.sprve.domain.entity.RoleMenu;
import com.sprve.domain.entity.UserRole;
import com.sprve.domain.vo.PageVo;
import com.sprve.mapper.RoleMapper;
import com.sprve.mapper.UserRoleMapper;
import com.sprve.response.ResponseResult;
import com.sprve.service.RoleMenuService;
import com.sprve.service.RoleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.sprve.domain.constants.SystemConstants.ROLE_STATUS_ENABLE;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private RoleMenuService roleMenuService;

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

    @Override
    public List<Role> selectRoleAll() {
        return list(Wrappers.<Role>lambdaQuery().eq(Role::getStatus, ROLE_STATUS_ENABLE));
    }

    @Override
    public List<Long> selectRoleIdByUserId(Long userId) {
        LambdaQueryWrapper<UserRole> userRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userRoleLambdaQueryWrapper.select(UserRole::getRoleId);
        userRoleLambdaQueryWrapper.eq(UserRole::getUserId,userId);
        List<Long> dataList = userRoleMapper.selectObjs(userRoleLambdaQueryWrapper);
        return dataList;
    }

    @Override
    public void insertRole(Role role) {
        save(role);
        System.out.println(role.getId());
        if(role.getMenuIds()!=null&&role.getMenuIds().length>0){
            insertRoleMenu(role);
        }
    }

    @Override
    public ResponseResult selectRolePage(Role role, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Role> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //目前没有根据id查询
//        lambdaQueryWrapper.eq(Objects.nonNull(role.getId()),Role::getId,role.getId());
        if(!ObjectUtil.isEmpty(role) && !ObjectUtil.isEmpty(role.getRoleName()))
            lambdaQueryWrapper.like(Role::getRoleName,role.getRoleName());
        if(!ObjectUtil.isEmpty(role) && !ObjectUtil.isEmpty(role.getStatus()))
            lambdaQueryWrapper.eq(Role::getStatus,role.getStatus());
        lambdaQueryWrapper.orderByAsc(Role::getRoleSort);

        Page<Role> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page,lambdaQueryWrapper);

        //转换成VO
        List<Role> roles = page.getRecords();

        PageVo pageVo = new PageVo();
        pageVo.setTotal(page.getTotal());
        pageVo.setRows(roles);
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public void updateRole(Role role) {
        updateById(role);
        roleMenuService.deleteRoleMenuByRoleId(role.getId());
        insertRoleMenu(role);
    }

    private void insertRoleMenu(Role role) {
        List<RoleMenu> roleMenuList = Arrays.stream(role.getMenuIds())
                .map(memuId -> new RoleMenu(role.getId(), memuId))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenuList);
    }
}
