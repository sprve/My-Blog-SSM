package com.sprve.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sprve.domain.entity.Article;
import com.sprve.domain.entity.Category;
import com.sprve.domain.vo.CategoryVo;
import com.sprve.domain.vo.ExcelCategoryVo;
import com.sprve.domain.vo.PageVo;
import com.sprve.exception.SystemException;
import com.sprve.mapper.ArticleMapper;
import com.sprve.mapper.CategoryMapper;
import com.sprve.response.CodeEnum;
import com.sprve.service.CategoryService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.ArrayList;
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

    @Override
    public List<CategoryVo> listAllCategory() {
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.eq(Category::getStatus,CATEGORY_STATUS_NORMAL);
        List<Category> categoryList =list(categoryLambdaQueryWrapper);
        List<CategoryVo> categoryVoList = BeanUtil.copyToList(categoryList,CategoryVo.class);
        return categoryVoList;
    }

    @Override
    public void export(HttpServletResponse response) throws Exception {
        List<Category> categoryList = list();
        if (ObjectUtil.isEmpty(categoryList)) {
            throw new SystemException(CodeEnum.SYSTEM_ERROR);
        }
        List<ExcelCategoryVo> excelCategoryVoList = BeanUtil.copyToList(categoryList, ExcelCategoryVo.class);
        //在内存操作，写到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        //自定义标题别名

        writer.addHeaderAlias("name", "名字");
        writer.addHeaderAlias("description", "描述");
        writer.addHeaderAlias("status", "状态(0正常,1禁用)");

        //只保留别名的数据
        writer.setOnlyAlias(true);
        // 默认配置
        writer.write(excelCategoryVoList, true);
        // 设置标题
        String fileName = URLEncoder.encode("Category", "UTF-8");
        //Content-disposition是MIME协议的扩展，MIME协议指示MIME用户代理如何显示附加的文件。
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        // 设置content—type
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset:utf-8");
        ServletOutputStream outputStream = response.getOutputStream();

        //将Writer刷新到OutPut
        writer.flush(outputStream, true);
        outputStream.close();
        writer.close();
    }

    @Override
    public PageVo selectCategoryPage(Category category, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();

        if(!ObjectUtil.isEmpty(category) && !ObjectUtil.isEmpty(category.getName()))
            queryWrapper.like(Category::getName, category.getName());
        if(!ObjectUtil.isEmpty(category) && !ObjectUtil.isEmpty(category.getStatus()))
            queryWrapper.eq(Category::getStatus, category.getStatus());

        Page<Category> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);

        //转换成VO
        List<Category> categories = page.getRecords();

        PageVo pageVo = new PageVo(categories,page.getTotal());
        return pageVo;
    }
}
