package com.sprve.service;

import com.sprve.domain.entity.User;
import com.sprve.domain.vo.BlogUserLoginVo;
import jakarta.servlet.http.HttpServletRequest;

public interface BlogLoginService {
    public abstract BlogUserLoginVo login(User user);

    public abstract void logout();
}
