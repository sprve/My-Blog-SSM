package com.sprve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sprve.domain.entity.User;
import com.sprve.domain.vo.UserInfoVo;
import com.sprve.response.ResponseResult;

public interface UserService extends IService<User> {
    UserInfoVo userInfo();

    void updateUserInfo(User user);

    void register(User user);

    ResponseResult selectUserPage(User user, Integer pageNum, Integer pageSize);

    boolean checkUserNameUnique(String userName);

    boolean checkPhoneUnique(User user);

    boolean checkEmailUnique(User user);

    ResponseResult addUser(User user);

    void updateUser(User user);
}
