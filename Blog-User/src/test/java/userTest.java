import com.sprve.UserApplication;
import com.sprve.service.ArticleService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes= UserApplication.class)
@RunWith(SpringRunner.class)
@Slf4j
public class userTest {

    @Resource
    ArticleService articleService;

    @Test
    public void t(){

        System.out.println(articleService.getById(1).toString());
        log.info("测试中");
    }
}