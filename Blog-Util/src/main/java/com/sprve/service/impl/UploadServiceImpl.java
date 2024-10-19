package com.sprve.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.sprve.exception.SystemException;
import com.sprve.response.CodeEnum;
import com.sprve.service.UploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class UploadServiceImpl implements UploadService {

    @Value("${oss.accessKey}")
    private String accessKey;

    @Value("${oss.secretKey}")
    private String secretKey;

    @Value("${oss.endPoint}")
    private String endPoint;

    @Value("${oss.bucketName}")
    private String bucketName;

    @Override
    public String uploadImg(MultipartFile img){
        String originalFileName =  img.getOriginalFilename();
        if(!originalFileName.endsWith(".png"))
            throw new SystemException(CodeEnum.FILE_TYPE_ERROR);
        String filePath = generateFilePath(originalFileName);
        String url = uploadOss(img,filePath);
        return url;
    }

    private String uploadOss(MultipartFile multipartFile, String filePath){
        OSS ossClient = new OSSClientBuilder().build(endPoint,accessKey,secretKey);
        InputStream inputStream;
        try {
            inputStream = multipartFile.getInputStream();
        }
        catch (Exception e){
            throw  new SystemException(CodeEnum.SYSTEM_ERROR);
        }
        try{
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, filePath, inputStream);
            PutObjectResult result=ossClient.putObject(putObjectRequest);
        }
        catch (OSSException oe){
            throw  new SystemException(CodeEnum.SYSTEM_ERROR);
        }
        catch (ClientException ce){
            throw  new SystemException(CodeEnum.SYSTEM_ERROR);
        }
        finally {
            if (!ObjectUtil.isEmpty(ossClient)) {
                ossClient.shutdown();
            }
        }
        return "https://"+bucketName+"."+endPoint+"/"+filePath;
    }

    private String generateFilePath(String fileName){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
        String datePath = sdf.format(new Date());

        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        int index = fileName.lastIndexOf(".");
        String fileType = fileName.substring(index);

        return datePath+uuid+fileType;
    }
}
