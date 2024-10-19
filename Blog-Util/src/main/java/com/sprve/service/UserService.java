package com.sprve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sprve.domain.entity.User;
import com.sprve.domain.vo.UserInfoVo;

public interface UserService extends IService<User> {
    UserInfoVo userInfo();

    void updateUserInfo(User user);

    void register(User user);
}
