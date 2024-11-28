package com.sprve;

import com.github.yulichang.query.MPJLambdaQueryWrapper;
import com.sprve.domain.entity.Article;
import com.sprve.mapper.ArticleMapper;
import com.sprve.mapper.CategoryMapper;
import com.sprve.service.ArticleService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest(classes= UserApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Slf4j
public class userTest {

    @Resource
    ArticleService articleService;

    @Resource
    ArticleMapper articleMapper;

    @Resource
    CategoryMapper categoryMapper;

    @Test
    public void list(){
        MPJLambdaQueryWrapper<Article> queryWrapper = new MPJLambdaQueryWrapper();
        queryWrapper.selectAll(Article.class);
        List<Article> articleList = articleMapper.selectJoinList(queryWrapper);
        for(Article article : articleList){
            log.info(article.getId()+" "+article.getCategoryName());
        }
        log.info("测试中");

    }
}