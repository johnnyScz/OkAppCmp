package com.xinyu.newdiggtest.bean;

/**
 * 基础回调接口
 * @param <T>
 */
public interface BaseModel<T> {

    public void onSuccess(T t);

    public void onFailed(String msg);

}
