package com.sprve.runner;

import cn.hutool.http.HttpRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
