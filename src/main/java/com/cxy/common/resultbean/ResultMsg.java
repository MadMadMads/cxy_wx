package com.cxy.common.resultbean;


import com.cxy.common.enums.ResultStatus;

import java.io.Serializable;

public class ResultMsg<T> extends AbstractResult implements Serializable {
    private static final long serialVersionUID = 867933019328199779L;
    private T data;
    private Integer count;

    public ResultMsg(ResultStatus status, String message) {
        super(status, message);
    }
    public ResultMsg(ResultStatus status) {
        super(status);
    }
    public static <T> ResultMsg<T> build() {
        return new ResultMsg(ResultStatus.SUCCESS, "操作成功");
    }

    public static <T> ResultMsg<T> build(String message) {
        return new ResultMsg(ResultStatus.SUCCESS, message);
    }

    public static <T> ResultMsg<T> error(ResultStatus status) {
        return new ResultMsg<T>(status);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getCount() {
        return this.count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void success(T value) {
        this.success();
        this.data = value;
        this.count = 0;
    }

}
