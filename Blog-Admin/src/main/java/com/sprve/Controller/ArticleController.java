package com.sprve.controller;

import com.sprve.domain.dto.AddArticleDto;
import com.sprve.response.ResponseResult;
import com.sprve.service.ArticleService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Resource
    ArticleService articleService;

    @PostMapping
    public ResponseResult add(@RequestBody AddArticleDto addArticleDto){
        articleService.add(addArticleDto);
        return ResponseResult.okResult();
    }


}
