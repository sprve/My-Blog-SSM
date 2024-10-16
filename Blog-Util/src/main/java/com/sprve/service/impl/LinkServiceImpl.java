package com.sprve.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sprve.domain.entity.Link;
import com.sprve.domain.vo.LinkVo;
import com.sprve.mapper.LinkMapper;
import com.sprve.service.LinkService;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.sprve.domain.constants.SystemConstants.LINK_STATUS_NORMAL;

@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {
    @Override
    public List<LinkVo> getAllLink() {
        LambdaQueryWrapper<Link> linkLambdaQueryWrapper = new LambdaQueryWrapper();
        linkLambdaQueryWrapper.eq(Link::getStatus,LINK_STATUS_NORMAL);
        List<Link> linkList = list(linkLambdaQueryWrapper);
        List<LinkVo> linkVoList = BeanUtil.copyToList(linkList, LinkVo.class);
        return linkVoList;
    }
}
