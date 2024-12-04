package com.sprve.controller;

import cn.hutool.core.bean.BeanUtil;
import com.sprve.domain.entity.LoginUser;
import com.sprve.domain.entity.User;
import com.sprve.domain.vo.AdminUserInfoVo;
import com.sprve.domain.vo.UserInfoVo;
import com.sprve.response.ResponseResult;
import com.sprve.service.LoginService;
import com.sprve.service.MenuService;
import com.sprve.service.RoleService;
import com.sprve.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class LoginController {

    @Resource
    private LoginService loginService;

    @Resource
    private MenuService menuService;

    @Resource
    private RoleService roleService;

    @Resource
    private UserService userService;

    @PostMapping("/user/login")
    public ResponseResult login2(@RequestBody User user) {
        Map<String,String> data = new HashMap<>();
        data.put("token",loginService.login(user));
        return ResponseResult.okResult(data);
    }

    @PostMapping("/user/logout")
    public ResponseResult logout() {
        loginService.logout();
        return ResponseResult.okResult();
    }

    @GetMapping("/getInfo")
    public ResponseResult getInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser =(LoginUser)authentication.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        List<String> perms = menuService.selectPermsByUserId(userId);
        List<String> roles = roleService.selectRolesByUserId(userId);
        User user = userService.getById(userId);
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtil.copyProperties(user,userInfoVo);
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms, roles, userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }
}
