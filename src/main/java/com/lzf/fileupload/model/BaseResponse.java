package com.lzf.fileupload.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResponse {

    private int code;

    private String msg;

    private Object data;

    public BaseResponse(){}

    public BaseResponse(int code,String msg){
        this.code=code;
        this.msg=msg;
    }

    public BaseResponse(int code,String msg,Object data){
        this.code=code;
        this.msg=msg;
        this.data=data;
    }

}
