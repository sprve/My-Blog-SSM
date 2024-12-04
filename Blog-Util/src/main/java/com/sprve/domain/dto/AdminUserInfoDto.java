package com.sprve.domain.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserInfoDto {

    /** 用户ID */
    private Long userId;

    /** 角色ID */
    private Long roleId;

    private Long permissionId;

    //角色名称
    private String roleKey;

    //权限标识
    private String perms;
}
