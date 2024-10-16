package com.sprve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sprve.domain.entity.Link;
import com.sprve.domain.vo.LinkVo;

import java.util.List;

public interface LinkService extends IService<Link> {
    List<LinkVo> getAllLink();
}
