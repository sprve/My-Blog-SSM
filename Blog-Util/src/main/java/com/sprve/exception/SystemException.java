package com.sprve.exception;

import com.sprve.response.CodeEnum;

public class SystemException extends  RuntimeException{

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public SystemException(CodeEnum codeEnum){
        super(codeEnum.getMsg());
        this.code=codeEnum.getCode();
        this.msg= codeEnum.getMsg();
    }
}
