package com.sprve.controller;

import cn.hutool.core.util.ObjectUtil;
import com.sprve.domain.entity.LoginUser;
import com.sprve.domain.entity.Role;
import com.sprve.domain.entity.User;
import com.sprve.domain.vo.UserInfoAndRoleIdsVo;
import com.sprve.exception.SystemException;
import com.sprve.response.ResponseResult;
import com.sprve.service.RoleService;
import com.sprve.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sprve.response.CodeEnum.*;

@RestController
@RequestMapping("/system/user")
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private RoleService roleService;
    /**
     * 获取用户列表
     */
    @GetMapping("/list")
    public ResponseResult list(User user, Integer pageNum, Integer pageSize) {
        return userService.selectUserPage(user,pageNum,pageSize);
    }

    /**
     * 新增用户
     */
    @PostMapping
    public ResponseResult add(@RequestBody User user)
    {
        if(ObjectUtil.isEmpty(user.getUserName())){
            throw new SystemException(REQUIRE_USERNAME);
        }
        if (!userService.checkUserNameUnique(user.getUserName())){
            throw new SystemException(USERNAME_EXIST);
        }
        if (!userService.checkPhoneUnique(user)){
            throw new SystemException(PHONENUMBER_EXIST);
        }
        if (!userService.checkEmailUnique(user)){
            throw new SystemException(EMAIL_EXIST);
        }
        return userService.addUser(user);
    }

    /**
     * 根据用户编号获取详细信息
     */
    @GetMapping("/{userId}")
    public ResponseResult getUserInfoAndRoleIds(@PathVariable(value = "userId") Long userId)
    {
        List<Role> roles = roleService.selectRoleAll();
        User user = userService.getById(userId);
        //当前用户所具有的角色id列表
        List<Long> roleIds = roleService.selectRoleIdByUserId(userId);

        UserInfoAndRoleIdsVo vo = new UserInfoAndRoleIdsVo(user,roles,roleIds);
        return ResponseResult.okResult(vo);
    }

    /**
     * 修改用户
     */
    @PutMapping
    public ResponseResult edit(@RequestBody User user) {
        userService.updateUser(user);
        return ResponseResult.okResult();
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{userIds}")
    public ResponseResult remove(@PathVariable List<Long> userIds) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser =(LoginUser)authentication.getPrincipal();
        if(userIds.contains(loginUser.getUser().getId())){
            return ResponseResult.errorResult(500,"不能删除当前你正在使用的用户");
        }
        userService.removeByIds(userIds);
        return ResponseResult.okResult();
    }
}
