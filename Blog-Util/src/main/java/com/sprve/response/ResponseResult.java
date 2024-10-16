package com.sprve.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult<T> implements Serializable {
    private Integer code;
    private String msg;
    private T data;

    public ResponseResult(){
        this.code= CodeEnum.SUCCESS.getCode();
        this.msg=CodeEnum.SUCCESS.getMsg();
    }

    public ResponseResult(Integer code,String msg){
        this.code=code;
        this.msg=msg;
    }

    public ResponseResult(Integer code,T data){
        this.code=code;
        this.data=data;
    }

    public ResponseResult(Integer code,String msg,T data){
        this.code=code;
        this.msg=msg;
        this.data=data;
    }

    public ResponseResult<?> ok(T data){
        this.data=data;
        return this;
    }

    public ResponseResult<?> ok(Integer code ,T data){
        this.code=code;
        this.data=data;
        return this;
    }

    public ResponseResult<?> ok(Integer code ,String msg,T data){
        this.code=code;
        this.msg=msg;
        this.data=data;
        return this;
    }

    public ResponseResult<?> error(Integer code ,String msg){
        this.code=code;
        this.msg=msg;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static ResponseResult setCodeEnum(CodeEnum enums){
        return  okResult(enums.getCode(),enums.getMsg());
    }

    public static ResponseResult setCodeEnum(CodeEnum enums,String msg){
        return  okResult(enums.getCode(),msg);
    }


    public static ResponseResult okResult(){
        ResponseResult result = new ResponseResult();
        return  result;
    }

    public static ResponseResult okResult(Integer code,String msg){
        ResponseResult result = new ResponseResult();
        return  result.ok(code,msg);
    }

    public static ResponseResult okResult(Object data){
        ResponseResult result = setCodeEnum(CodeEnum.SUCCESS, CodeEnum.SUCCESS.getMsg());
        if(data!=null) {
            result.setData(data);
        }
        return result;
    }

    public static ResponseResult errorResult(Integer code,String msg){
       ResponseResult result = new ResponseResult();
       return  result.error(code,msg);
    }

    public static ResponseResult errorResult(CodeEnum enums){
        return setCodeEnum(enums,enums.getMsg());
    }

    public static ResponseResult errorResult(CodeEnum enums, String msg){
        return setCodeEnum(enums,msg);
    }
}