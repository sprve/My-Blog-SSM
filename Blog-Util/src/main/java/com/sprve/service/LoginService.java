package com.sprve.service;

import com.sprve.domain.entity.User;
import com.sprve.domain.vo.AdminUserInfoVo;

public interface LoginService {
    String login(User user);

    void logout();

}
