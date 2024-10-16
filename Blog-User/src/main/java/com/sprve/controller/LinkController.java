package com.sprve.controller;

import com.sprve.domain.vo.LinkVo;
import com.sprve.response.ResponseResult;
import com.sprve.service.LinkService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/link")
public class LinkController {

    @Resource
    LinkService linkService;

    @GetMapping("/getAllLink")
    public ResponseResult getAllLink(){
        List<LinkVo> data = linkService.getAllLink();
        return  ResponseResult.okResult(data);
    }
}
