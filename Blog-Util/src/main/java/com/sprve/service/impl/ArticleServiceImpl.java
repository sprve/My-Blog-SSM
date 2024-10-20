package com.sprve.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sprve.Util.RedisUtil;
import com.sprve.domain.entity.Article;
import com.sprve.domain.entity.Category;
import com.sprve.domain.vo.*;
import com.sprve.mapper.ArticleMapper;
import com.sprve.mapper.CategoryMapper;
import com.sprve.service.ArticleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.sprve.domain.constants.SystemConstants.ARTICLE_STATUS_NORMAL;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public List<HotArticleVo> hotArticleList() {
        //根据热点数分页查询前10条文章
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.eq(Article::getStatus,ARTICLE_STATUS_NORMAL);
        articleLambdaQueryWrapper.orderByDesc(Article::getViewCount);
        Page<Article> page = new Page<>(1,10);
        page(page,articleLambdaQueryWrapper);
        List<Article> articleList = page.getRecords();
        List<HotArticleVo> hotArticleVoList = BeanUtil.copyToList(articleList,HotArticleVo.class);
        return hotArticleVoList;
    }

    @Override
    public PageVo articleList(Long categoryId,Integer pageNum,Integer pageSize) {
        //根据规则分页查询文章表的数据
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper();
        if (ObjectUtil.isEmpty(categoryId)){
            articleLambdaQueryWrapper.eq(Article::getCategoryId,categoryId);
        }
        articleLambdaQueryWrapper.eq(Article::getStatus,ARTICLE_STATUS_NORMAL);
        articleLambdaQueryWrapper.orderByDesc(Article::getIsTop);
        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page,articleLambdaQueryWrapper);
        List<Article> articleList = page.getRecords();
        //根据分类Id查询分类表的分类名
        for(Article article : articleList){
            LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper();
            categoryLambdaQueryWrapper.eq(Category::getId,article.getCategoryId());
            article.setCategoryName(categoryMapper.selectOne(categoryLambdaQueryWrapper).getName());
        }
        List<ArticleListVo> articleListVoList = BeanUtil.copyToList(articleList, ArticleListVo.class);
        PageVo pageVo = new PageVo(articleListVoList,page.getTotal());
        return pageVo;
    }

    @Override
    public ArticleDetailVo getArticleDetail(Long id) {
        Article article = getById(id);
        Integer viewCount = redisUtil.getCacheMapValue("article:viewCount", article.getId().toString());
        article.setViewCount(viewCount.longValue());
        ArticleDetailVo articleDetailVo = new ArticleDetailVo();
        BeanUtil.copyProperties(article,articleDetailVo);
        articleDetailVo.setCategoryName(categoryMapper.selectById(articleDetailVo.getCategoryId()).getName());
        return  articleDetailVo;
    }

    @Override
    public void updateViewCount(Long id) {
        Integer viewCount = redisUtil.getCacheMapValue("article:viewCount", id.toString());
        redisUtil.setCacheMapValue("article:viewCount", id.toString(),viewCount+1);
    }

    @Override
    public void updateViewCountList() {

        Map<String,Integer> viewCountMap=redisUtil.getCacheMap("article:viewCount");
        List<Article> articleList = viewCountMap.entrySet().stream().map(entry -> {
            Article article = new Article();
            article.setId(Long.valueOf(entry.getKey()));
            article.setViewCount(Long.valueOf(entry.getValue()));
            return article;
        }).collect(Collectors.toList());
        for(Article article : articleList){
            LambdaUpdateWrapper<Article> articleLambdaUpdateWrapper =new LambdaUpdateWrapper<>();
            articleLambdaUpdateWrapper.set(Article::getViewCount,article.getViewCount());
            articleLambdaUpdateWrapper.eq(Article::getId,article.getId());
            update(articleLambdaUpdateWrapper);
        }
    }
}
