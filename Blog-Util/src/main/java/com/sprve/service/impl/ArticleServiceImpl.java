package com.sprve.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sprve.Util.RedisUtil;
import com.sprve.domain.dto.AddArticleDto;
import com.sprve.domain.dto.ArticleDto;
import com.sprve.domain.entity.Article;
import com.sprve.domain.entity.ArticleTag;
import com.sprve.domain.entity.Category;
import com.sprve.domain.vo.*;
import com.sprve.mapper.ArticleMapper;
import com.sprve.mapper.CategoryMapper;
import com.sprve.response.ResponseResult;
import com.sprve.service.ArticleService;
import com.sprve.service.ArticleTagService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
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
    private ArticleTagService articleTagService;

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
        //根据文章Id获取文章详细信息
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

    @Override
    public void add(AddArticleDto addArticleDto) {
        Article article = new Article();
        BeanUtil.copyProperties(addArticleDto,article);
        save(article);
        List<ArticleTag> articleTags = addArticleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());

        //添加 博客和标签的关联
        articleTagService.saveBatch(articleTags);
    }

    @Override
    public PageVo selectArticlePage(Article article, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper =new LambdaQueryWrapper<>();
        if(!ObjectUtil.isEmpty(article.getTitle()))
            articleLambdaQueryWrapper.like(Article::getTitle,article.getTitle());
        if(!ObjectUtil.isEmpty(article.getSummary()))
            articleLambdaQueryWrapper.like(Article::getSummary,article.getSummary());
        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page,articleLambdaQueryWrapper);
        List<Article> articleList =page.getRecords();
        PageVo pageVo = new PageVo(articleList,page.getTotal());
        return pageVo;
    }

    @Override
    public ArticleVo getInfo(Long id) {
        Article article = getById(id);
        //获取关联标签
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId,article.getId());
        List<ArticleTag> articleTags = articleTagService.list(articleTagLambdaQueryWrapper);
        List<Long> tags = articleTags.stream().map(articleTag -> articleTag.getTagId()).collect(Collectors.toList());
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);
        articleVo.setTags(tags);
        return articleVo;
    }

    @Override
    public void edit(ArticleDto articleDto) {
        Article article = new Article();
        BeanUtil.copyProperties(articleDto, article);
        //更新博客信息
        updateById(article);
        //删除原有的 标签和博客的关联
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId,article.getId());
        articleTagService.remove(articleTagLambdaQueryWrapper);
        //添加新的博客和标签的关联信息
        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(articleDto.getId(), tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);
    }
}
