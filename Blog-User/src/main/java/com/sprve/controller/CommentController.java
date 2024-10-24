package com.sprve.controller;

import com.sprve.domain.entity.Comment;
import com.sprve.domain.vo.PageVo;
import com.sprve.response.ResponseResult;
import com.sprve.service.CommentService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import static com.sprve.domain.constants.SystemConstants.COMMENT_ARTICLE;
import static com.sprve.domain.constants.SystemConstants.COMMENT_LINK;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    CommentService commentService;

    @GetMapping("/commentList")
    public ResponseResult commentList(@RequestParam Long articleId,
                                      @RequestParam(defaultValue = "1") Integer pageNum,
                                      @RequestParam(defaultValue = "10") Integer pageSize){
        PageVo data =commentService.commentList(COMMENT_ARTICLE,articleId,pageNum,pageSize);
        return  ResponseResult.okResult(data);
    }

    @PostMapping
    public ResponseResult addComment(@RequestBody Comment comment){
        commentService.addComment(comment);
        return ResponseResult.okResult();
    }

    @GetMapping("/linkCommentList")
    public ResponseResult linkCommentList(@RequestParam(defaultValue = "1") Integer pageNum,
                                          @RequestParam(defaultValue = "10") Integer pageSize){
        PageVo data = commentService.commentList(COMMENT_LINK,null,pageNum,pageSize);
        return ResponseResult.okResult(data);
    }
}
