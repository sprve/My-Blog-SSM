package com.sprve.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.sprve.domain.entity.Category;
import com.sprve.domain.vo.CategoryVo;
import com.sprve.domain.vo.ExcelCategoryVo;
import com.sprve.exception.SystemException;
import com.sprve.response.CodeEnum;
import com.sprve.response.ResponseResult;
import com.sprve.service.CategoryService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    @GetMapping("/getCategoryList")
    public ResponseResult getCategoryList(){
        List<CategoryVo> data = categoryService.getCategoryList();
        return ResponseResult.okResult(data);
    }
}
