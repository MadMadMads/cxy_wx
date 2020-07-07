package com.cxy.common.exception;

import com.cxy.common.enums.ResultStatus;

/**
 * @author: Luo
 * @description:
 * @time: 2020/6/17 20:51
 * Modified By:
 */
public class GlobleException extends RuntimeException {
    private ResultStatus status;
    public GlobleException(ResultStatus status) {
        super();
        this.status = status;
    }
    public ResultStatus getStatus() {
        return status;
    }

    public void setStatus(ResultStatus status) {
        this.status = status;
    }
}

