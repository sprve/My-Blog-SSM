package com.sprve.Controller;

import com.sprve.domain.entity.Tag;
import com.sprve.response.ResponseResult;
import com.sprve.service.TagService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagController {

    @Resource
    TagService tagService;

    @GetMapping("allList")
    public ResponseResult allList(){
        List<Tag> tagList = tagService.list();
        return ResponseResult.okResult(tagList);
    }
}
