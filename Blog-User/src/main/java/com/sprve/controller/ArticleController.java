package com.sprve.controller;

import com.sprve.domain.vo.ArticleDetailVo;
import com.sprve.domain.vo.HotArticleVo;
import com.sprve.domain.vo.PageVo;
import com.sprve.response.ResponseResult;
import com.sprve.service.ArticleService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @GetMapping("/hotArticleList")
    public ResponseResult hotArticleList(){
        List<HotArticleVo> data= articleService.hotArticleList();
        return ResponseResult.okResult(data);
    }

    @GetMapping("/articleList")
    public ResponseResult articleList(@RequestParam(required = false) Long categoryId,
                                      @RequestParam(defaultValue = "1") Integer pageNum,
                                      @RequestParam(defaultValue = "10") Integer pageSize){
        PageVo data= articleService.articleList(categoryId,pageNum,pageSize);
        return ResponseResult.okResult(data);
    }

    @GetMapping("/{id}")
    public ResponseResult getArticleDetail(@PathVariable Long id){
        ArticleDetailVo data = articleService.getArticleDetail(id);
        return  ResponseResult.okResult(data);
    }

}