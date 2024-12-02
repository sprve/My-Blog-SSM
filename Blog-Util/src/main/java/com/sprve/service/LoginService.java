package com.sprve.service;

import com.sprve.domain.entity.User;

public interface LoginService {
    String login(User user);

    void logout();
}
