package com.sprve.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkVo {
    //Id
    private Long id;
    //网站名字
    private String name;
    //网站Logo
    private String logo;
    //网站描述
    private String description;
    //网站地址
    private String address;
}
