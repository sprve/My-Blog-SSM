import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.query.MPJLambdaQueryWrapper;
import com.sprve.UserApplication;
import com.sprve.domain.entity.Article;
import com.sprve.domain.entity.Category;
import com.sprve.mapper.ArticleMapper;
import com.sprve.mapper.CategoryMapper;
import com.sprve.service.ArticleService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes= UserApplication.class)
@RunWith(SpringRunner.class)
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
        log.info("测试中");
    }
}