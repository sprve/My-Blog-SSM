package com.sprve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sprve.domain.dto.AddArticleDto;
import com.sprve.domain.entity.Article;
import com.sprve.domain.vo.ArticleDetailVo;
import com.sprve.domain.vo.HotArticleVo;
import com.sprve.domain.vo.PageVo;
import com.sprve.response.ResponseResult;

import java.util.List;

public interface ArticleService extends IService<Article> {
    public abstract List<HotArticleVo> hotArticleList();
    public abstract PageVo articleList(Long categoryId, Integer pageNum, Integer pageSize);
    public abstract ArticleDetailVo getArticleDetail(Long id);

    void updateViewCount(Long id);

    void updateViewCountList();

    void add(AddArticleDto addArticleDto);
}
