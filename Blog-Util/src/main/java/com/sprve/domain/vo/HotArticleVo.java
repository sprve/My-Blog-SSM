package com.sprve.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotArticleVo implements Serializable {
    //Id
    private  Long id;
    //文章标题
    private  String title;
    //文章观看次数
    private  Long viewCount;
}