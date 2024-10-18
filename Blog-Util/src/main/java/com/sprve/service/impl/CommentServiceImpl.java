package com.sprve.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sprve.domain.entity.Comment;
import com.sprve.domain.vo.CommentVo;
import com.sprve.domain.vo.PageVo;
import com.sprve.exception.SystemException;
import com.sprve.mapper.CommentMapper;
import com.sprve.mapper.UserMapper;
import com.sprve.response.CodeEnum;
import com.sprve.service.CommentService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Resource
    private UserMapper userMapper;

    @Override
    public PageVo commentList(String commentType,Long articleId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        commentLambdaQueryWrapper
                .eq(!ObjectUtil.isEmpty(articleId),Comment::getArticleId,articleId)
                .eq(Comment::getType,commentType)
                .eq(Comment::getRootId,-1);
        Page<Comment> page = new Page<>(pageNum,pageSize);
        page(page,commentLambdaQueryWrapper);

        List<CommentVo> commentVoList =toCommentVoList(page.getRecords());
        for(CommentVo commentVo : commentVoList){
            List<CommentVo> children = getChildren(commentVo.getId());
            commentVo.setChildren(children);
        }

        PageVo pageVo = new PageVo(commentVoList,page.getTotal());
        return  pageVo;

    }

    @Override
    public void addComment(Comment comment){
        if(ObjectUtil.isEmpty(comment.getContent()))
            throw new SystemException(CodeEnum.CONTENT_NOT_NULL);
        save(comment);
    }

    private List<CommentVo> toCommentVoList(List<Comment> commentList){
        List<CommentVo> commentVoList = BeanUtil.copyToList(commentList, CommentVo.class);
        for(CommentVo commentVo:commentVoList) {
            String nickName = userMapper.selectById(commentVo.getCreateBy()).getNickName();
            commentVo.setUsername(nickName);
            if(commentVo.getToCommentUserId()!=-1){
                String toCommentUserName = userMapper.selectById(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentUserName(toCommentUserName);
            }
        }
        return commentVoList;
    }

    private List<CommentVo> getChildren(Long id){
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        commentLambdaQueryWrapper.eq(Comment::getRootId,id);
        commentLambdaQueryWrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> commentList = list(commentLambdaQueryWrapper);
        List<CommentVo> commentVoList = toCommentVoList(commentList);
        return commentVoList;
    }
}