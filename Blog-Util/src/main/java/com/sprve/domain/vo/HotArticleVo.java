package com.sprve.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotArticleVo implements Serializable {
    private  Long id;
    private  String title;
    private  Long viewCount;
}