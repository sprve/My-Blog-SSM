package com.sprve.controller;

import com.sprve.domain.vo.HotArticleVo;
import com.sprve.response.ResponseResult;
import com.sprve.service.ArticleService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
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
}
