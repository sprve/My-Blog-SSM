package com.sprve.controller;

import com.sprve.response.ResponseResult;
import com.sprve.service.UploadService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {

    @Resource
    private UploadService uploadService;

    @PostMapping("/upload")
    public ResponseResult upload(MultipartFile img) {
        String data=uploadService.uploadImg(img);
        return ResponseResult.okResult(data);
    }
}
