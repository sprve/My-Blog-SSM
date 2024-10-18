package com.sprve.controller;

import com.sprve.response.ResponseResult;
import com.sprve.service.UploadService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
public class UploadController {

    @Resource
    UploadService uploadService;

    @PostMapping("/upload")
    public ResponseResult upload(MultipartFile multipartFile){
        String data=uploadService.uploadImg(multipartFile);
        return  ResponseResult.okResult(data);
    }
}
