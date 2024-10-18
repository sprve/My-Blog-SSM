package com.sprve.exception;

import com.sprve.response.CodeEnum;
import com.sprve.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(SystemException.class)
    public ResponseResult SystemException(SystemException systemException){
        log.error(systemException.getMsg());
        return ResponseResult.errorResult(systemException.getCode(),systemException.getMsg());
    }

    @ExceptionHandler(Exception.class)
    public ResponseResult Exception(Exception exception){
        log.error(exception.getMessage());
        return ResponseResult.errorResult(CodeEnum.SYSTEM_ERROR.getCode(),exception.getMessage());
    }
}
