package com.example.dto;

import lombok.Data;

@Data
public class ResultDTO<T> {
    private Integer code;  // 状态码：200成功，400失败，403无权限
    private String message;    // 提示信息
    private T data;   // 具体数据（泛型）

    private ResultDTO(Integer code,String message,T data){
        this.code=code;
        this.message=message;
        this.data=data;
    }
    public static <T> ResultDTO<T> success(T data){
        return new ResultDTO<>(200,"success",data);
    }
    public static <T> ResultDTO<T> success(String message,T data){
        return new ResultDTO<>(200,message,data);
    }
    public static <T> ResultDTO<T> error(String message){
        return new ResultDTO<>(400,message,null);
    }
    public static <T> ResultDTO<T> error(Integer code,String message){
        return new ResultDTO<>(code,message,null);
    }

}
