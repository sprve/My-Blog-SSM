package com.sprve.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sprve.domain.entity.Category;
import com.sprve.domain.entity.Link;
import com.sprve.domain.vo.LinkVo;
import com.sprve.domain.vo.PageVo;
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

    @Override
    public PageVo selectLinkPage(Link link, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper();

        if(!ObjectUtil.isEmpty(link) && !ObjectUtil.isEmpty(link.getName()))
            queryWrapper.like(Link::getName, link.getName());
        if(!ObjectUtil.isEmpty(link) && !ObjectUtil.isEmpty(link.getStatus()))
            queryWrapper.eq(Link::getStatus, link.getStatus());

        Page<Link> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);

        //转换成VO
        List<Link> linkList = page.getRecords();

        PageVo pageVo = new PageVo(linkList,page.getTotal());
        return pageVo;
    }
}
