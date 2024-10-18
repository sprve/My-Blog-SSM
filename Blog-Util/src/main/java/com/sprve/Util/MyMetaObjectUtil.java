package com.sprve.Util;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.sprve.domain.entity.LoginUser;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MyMetaObjectUtil implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        LoginUser loginUser=(LoginUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId=loginUser.getUser().getId();
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("createBy",userId , metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
        this.setFieldValByName("updateBy", userId, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        LoginUser loginUser=(LoginUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId=loginUser.getUser().getId();
        this.setFieldValByName("updateTime", new Date(), metaObject);
        this.setFieldValByName("updateBy",userId , metaObject);
    }
}
