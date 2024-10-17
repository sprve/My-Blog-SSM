package com.sprve.service;

import com.sprve.domain.entity.User;
import com.sprve.domain.vo.BlogUserLoginVo;

public interface BlogLoginService {
    public abstract BlogUserLoginVo login(User user);
}
