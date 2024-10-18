package com.sprve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sprve.domain.entity.Comment;
import com.sprve.domain.vo.CommentVo;
import com.sprve.domain.vo.PageVo;
import com.sprve.mapper.CommentMapper;
import com.sprve.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Override
    public PageVo commentList(Long id, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        commentLambdaQueryWrapper
                .eq(Comment::getArticleId,id)
                .eq(Comment::getType,0)
                .eq(Comment::getRootId,-1);
        Page<Comment> page = new Page<>(pageNum,pageSize);
        page(page,commentLambdaQueryWrapper);
        List<Comment> commentRootList = page.getRecords();
        List<CommentVo> commentVoList = new ArrayList<>();
        for(Comment comment : commentRootList){
            LambdaQueryWrapper<Comment> commentChildLambdaQueryWrapper = new LambdaQueryWrapper<>();
            commentChildLambdaQueryWrapper
                    .eq(Comment::getRootId,comment.getId());
            List<Comment> commentChidList =list(commentChildLambdaQueryWrapper);
            CommentVo commentVo = new CommentVo(comment,commentChidList);
            commentVoList.add(commentVo);
        }
        PageVo pageVo = new PageVo(commentVoList,page.getTotal());
        return pageVo;
    }
}