package com.sprve.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogUserLoginVo {
    //Token
    private String token;
    //用户信息
    private UserInfoVo userInfo;
}
