package com.sprve.domain.vo;

import cn.hutool.core.bean.BeanUtil;
import com.sprve.domain.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentVo extends Comment{
    private List<Comment> children;

    public CommentVo(Comment comment,List<Comment> children){
        BeanUtil.copyProperties(comment,this);
        this.children = children;
    }
}
