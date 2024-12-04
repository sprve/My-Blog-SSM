package com.sprve;

import com.sprve.service.MenuService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Slf4j
public class AdminTest {

    @Resource
    MenuService menuService;

    @Test
    public void list(){
        List<String> dataList = menuService.selectPermsByUserId("1");
        for(String data : dataList)
            log.info(data);
    }
}
