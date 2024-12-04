package com.sprve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sprve.domain.entity.Link;
import com.sprve.domain.vo.LinkVo;
import com.sprve.domain.vo.PageVo;

import java.util.List;

public interface LinkService extends IService<Link> {
    List<LinkVo> getAllLink();

    PageVo selectLinkPage(Link link, Integer pageNum, Integer pageSize);
}
