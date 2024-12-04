package com.sprve.controller;

import cn.hutool.core.bean.BeanUtil;
import com.sprve.domain.dto.AddTagDto;
import com.sprve.domain.entity.Tag;
import com.sprve.domain.vo.PageVo;
import com.sprve.domain.vo.TagVo;
import com.sprve.response.ResponseResult;
import com.sprve.service.TagService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Resource
    TagService tagService;

    @GetMapping("/list")
    public ResponseResult list(@RequestParam(defaultValue = "1") Integer pageNum,
                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                  @RequestParam(required = false) String name){
        PageVo data = tagService.pageTagList(pageNum,pageSize,name);
        return ResponseResult.okResult(data);
    }

    @PostMapping
    public ResponseResult insert(@RequestBody AddTagDto addtagDto){
        Tag tag = new Tag();
        BeanUtil.copyProperties(addtagDto,tag);
        tagService.save(tag);
        return  ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable Long id){
        tagService.removeById(id);
        return  ResponseResult.okResult();
    }

    @PutMapping
    public ResponseResult update(@RequestBody Tag tag){
        tagService.updateById(tag);
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    public ResponseResult getInfo(@PathVariable Long id){
        Tag tag = tagService.getById(id);
        return ResponseResult.okResult(tag);
    }

    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        List<TagVo> data = tagService.listAllTag();
        return ResponseResult.okResult(data);
    }
}
