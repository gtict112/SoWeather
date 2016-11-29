package com.example.administrator.soweather.com.example.administrator.soweather.mode;

/**
 * Created by Administrator on 2016/10/11.
 */

public class Result<T> {
    private boolean success;
    private boolean finish = true;
    private String errorMessage;
    private T data;
    public boolean isSuccess() {
        return success;
    }

    public Result<T> setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Result<T> setErrorMessage(String message) {
        this.errorMessage = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public Result<T> setData(T data) {
        this.data = data;
        return this;
    }
}
