package com.sprve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sprve.domain.entity.Comment;
import com.sprve.domain.vo.PageVo;

public interface CommentService extends IService<Comment> {
    PageVo commentList(String commentType,Long articleId, Integer pageNum, Integer pageSize);

    void addComment(Comment comment);
}
