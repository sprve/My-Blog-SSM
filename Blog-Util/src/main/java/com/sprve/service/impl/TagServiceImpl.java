package com.sprve.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sprve.domain.entity.Link;
import com.sprve.domain.entity.Tag;
import com.sprve.domain.vo.PageVo;
import com.sprve.domain.vo.TagVo;
import com.sprve.mapper.TagMapper;
import com.sprve.service.TagService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
    @Override
    public PageVo pageTagList(Integer pageNum, Integer pageSize, String tagName) {
        LambdaQueryWrapper<Tag> tagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(!ObjectUtil.isEmpty(tagName))
            tagLambdaQueryWrapper.eq(Tag::getName,tagName);
        Page<Tag> page = new Page<>(pageNum,pageSize);
        page(page,tagLambdaQueryWrapper);
        List<Tag> tagList = page.getRecords();
        PageVo pageVo = new PageVo(tagList,page.getTotal());
        return pageVo;
    }

    @Override
    public List<TagVo> listAllTag() {
        List<Tag> tagList = list();
        List<TagVo> tagVoList = BeanUtil.copyToList(tagList, TagVo.class);
        return tagVoList;
    }
}
