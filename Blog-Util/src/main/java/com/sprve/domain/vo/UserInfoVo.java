package com.sprve.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserInfoVo {
    //Id
    private Long id;
    //昵称
    private String nickName;
    //头像
    private String avatar;
    //性别
    private String sex;
    //邮箱
    private String email;
}