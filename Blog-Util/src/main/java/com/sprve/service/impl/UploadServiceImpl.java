package com.sprve.service.impl;

import com.sprve.service.UploadService;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Data
public class UploadServiceImpl implements UploadService {
    @Override
    public String uploadImg(MultipartFile multipartFile) {
        String url;

        return url;
    }
}
