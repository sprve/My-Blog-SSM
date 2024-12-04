package com.sprve.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sprve.domain.entity.ArticleTag;
import com.sprve.mapper.ArticleTagMapper;
import com.sprve.service.ArticleTagService;
import org.springframework.stereotype.Service;

@Service
public class ArticleTagServiceImpl extends  ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {
}
