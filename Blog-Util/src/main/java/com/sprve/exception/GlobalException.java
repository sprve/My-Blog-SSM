package com.sprve.exception;

import com.sprve.response.CodeEnum;
import com.sprve.response.ResponseResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(SystemException.class)
    public ResponseResult SystemException(SystemException systemException){
        return ResponseResult.errorResult(systemException.getCode(),systemException.getMsg());
    }

    @ExceptionHandler(Exception.class)
    public ResponseResult Exception(Exception exception){
        return ResponseResult.errorResult(CodeEnum.SYSTEM_ERROR.getCode(),exception.getMessage());
    }
}
