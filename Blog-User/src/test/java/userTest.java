import com.sprve.UserApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes= UserApplication.class)
@RunWith(SpringRunner.class)
@Slf4j
public class userTest {

    private static final Logger log = LoggerFactory.getLogger(userTest.class);
    @Autowired
   // testservice ts;
    @Test
    public void t(){

   //     System.out.println(ts.getById(1).toString();
        log.info("");
    }
}