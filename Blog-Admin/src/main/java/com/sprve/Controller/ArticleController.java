package com.sprve.controller;

import com.sprve.domain.dto.AddArticleDto;
import com.sprve.domain.dto.ArticleDto;
import com.sprve.domain.entity.Article;
import com.sprve.domain.vo.ArticleVo;
import com.sprve.domain.vo.PageVo;
import com.sprve.response.ResponseResult;
import com.sprve.service.ArticleService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/list")
    public ResponseResult list(Article article, Integer pageNum, Integer pageSize)
    {
        PageVo pageVo = articleService.selectArticlePage(article,pageNum,pageSize);
        return ResponseResult.okResult(pageVo);
    }

    @GetMapping("/{id}")
    public ResponseResult getInfo(@PathVariable(value = "id")Long id){
        ArticleVo article = articleService.getInfo(id);
        return ResponseResult.okResult(article);
    }

    @PutMapping
    public ResponseResult edit(@RequestBody ArticleDto article){
        articleService.edit(article);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable Long id){
        articleService.removeById(id);
        return ResponseResult.okResult();
    }

}
