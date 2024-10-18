package com.sprve.controller;

import com.sprve.domain.vo.PageVo;
import com.sprve.response.ResponseResult;
import com.sprve.service.CommentService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    CommentService commentService;

    @GetMapping("/commentList")
    public ResponseResult commentList(@RequestParam Long id,
                                      @RequestParam(defaultValue = "1") Integer pageNum,
                                      @RequestParam(defaultValue = "10") Integer pageSize){
        PageVo data =commentService.commentList(id,pageNum,pageSize);
        return  ResponseResult.okResult(data);
    }
}
