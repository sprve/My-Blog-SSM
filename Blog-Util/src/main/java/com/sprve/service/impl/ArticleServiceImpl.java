package com.sprve.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sprve.domain.entity.Article;
import com.sprve.domain.vo.HotArticleVo;
import com.sprve.mapper.ArticleMapper;
import com.sprve.service.ArticleService;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.sprve.domain.constants.SystemConstants.ARTICLE_STATUS_NORMAL;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Override
    public List<HotArticleVo> hotArticleList() {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus,ARTICLE_STATUS_NORMAL);
        queryWrapper.orderByDesc(Article::getViewCount);
        Page<Article> page = new Page<>(1,10);
        page(page,queryWrapper);
        List<Article> articleList = page.getRecords();
        List<HotArticleVo> hotArticleVoList = BeanUtil.copyToList(articleList,HotArticleVo.class);
        return hotArticleVoList;
    }
}
