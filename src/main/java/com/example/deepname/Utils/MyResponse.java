package com.example.deepname.Utils;

public class MyResponse {
    private String msg;
    private Boolean isSuccess;
    private Object data;

    public static MyResponse buildSuccess() {
        MyResponse response = new MyResponse();
        response.setIsSuccess(true);
        return response;
    }

    public static MyResponse buildSuccess(Object data) {
        MyResponse response = new MyResponse();
        response.setIsSuccess(true);
        response.setData(data);
        return response;
    }

    public static MyResponse buildFailure(String msg) {
        MyResponse response = new MyResponse();
        response.setIsSuccess(false);
        response.setMsg(msg);
        return response;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
