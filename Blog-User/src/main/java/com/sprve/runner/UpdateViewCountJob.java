package com.sprve.runner;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UpdateViewCountJob{

    @Value("${server.port}")
    private String port;

    @Scheduled(cron = "30 * * * * ?")
    public void updateViewCount(){
        HttpRequest.put("http://localhost:"+port+"/article/updateViewCountList")
                   .execute();
    }
}
