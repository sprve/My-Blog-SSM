package com.sprve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sprve.domain.entity.Link;
import com.sprve.domain.entity.Tag;
import com.sprve.domain.vo.PageVo;
import com.sprve.domain.vo.TagVo;

import java.util.List;

public interface TagService extends IService<Tag> {
    PageVo pageTagList(Integer pageNum, Integer pageSize, String tagName);

    List<TagVo> listAllTag();
}
