package com.sprve.runner;

import com.sprve.Util.RedisUtil;
import com.sprve.domain.entity.Article;
import com.sprve.service.ArticleService;
import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ViewCountRunner implements CommandLineRunner {

    @Resource
    private ArticleService articleService;

    @Resource
    private  RedisUtil redisUtil;

    @Override
    public void run(String... args) throws Exception {
        List<Article> articleList = articleService.list();
        Map<String, Integer> viewCountMap = articleList.stream().collect(
                Collectors.toMap(article -> article.getId().toString(),
                                 article -> article.getViewCount().intValue()
                ));
        redisUtil.setCacheMap("article:viewCount",viewCountMap);
    }
}