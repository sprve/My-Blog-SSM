package com.sprve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sprve.domain.entity.Article;
import com.sprve.domain.vo.HotArticleVo;

import java.util.List;

public interface ArticleService extends IService<Article> {
    public abstract List<HotArticleVo> hotArticleList();
}
