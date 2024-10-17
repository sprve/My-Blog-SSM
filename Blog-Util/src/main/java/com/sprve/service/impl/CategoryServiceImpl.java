package com.sprve.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sprve.domain.entity.Article;
import com.sprve.domain.entity.Category;
import com.sprve.domain.vo.CategoryVo;
import com.sprve.mapper.ArticleMapper;
import com.sprve.mapper.CategoryMapper;
import com.sprve.service.CategoryService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.sprve.domain.constants.SystemConstants.ARTICLE_STATUS_NORMAL;
import static com.sprve.domain.constants.SystemConstants.CATEGORY_STATUS_NORMAL;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements  CategoryService {

    @Resource
    private ArticleMapper articleMapper;

    @Override
    public List<CategoryVo> getCategoryList() {
        //根据链式调用取得分类Id数据集合
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper= new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.eq(Article::getStatus,ARTICLE_STATUS_NORMAL);
        List<Article> articleList= articleMapper.selectList(articleLambdaQueryWrapper);
        Set<Long> categorieByIdSet = articleList
                .stream()
                .map(article -> article.getCategoryId())
                .collect(Collectors.toSet());
        //根据分类Id集合取得分类表的正常数据
        List<Category> categoryList = listByIds(categorieByIdSet);
        categoryList
                .stream()
                .filter(category -> CATEGORY_STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());
        List<CategoryVo> categoryVoList = BeanUtil.copyToList(categoryList,CategoryVo.class);
        return  categoryVoList;
    }
}
