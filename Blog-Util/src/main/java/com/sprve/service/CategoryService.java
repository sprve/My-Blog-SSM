package com.sprve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sprve.domain.entity.Category;
import com.sprve.domain.vo.CategoryVo;
import com.sprve.domain.vo.PageVo;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface CategoryService extends IService<Category> {
    public abstract List<CategoryVo> getCategoryList();

    List<CategoryVo> listAllCategory();

    void export(HttpServletResponse response) throws Exception;

    PageVo selectCategoryPage(Category category, Integer pageNum, Integer pageSize);
}
