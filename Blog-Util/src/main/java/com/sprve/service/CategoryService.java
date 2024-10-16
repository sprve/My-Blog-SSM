package com.sprve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sprve.domain.entity.Category;
import com.sprve.domain.vo.CategoryVo;

import java.util.List;

public interface CategoryService extends IService<Category> {
    public abstract List<CategoryVo> getCategoryList();
}
