package com.cj.common.en;

import lombok.AllArgsConstructor;

@AllArgsConstructor

public enum CommonError {

    UNKNOW_ERROR("系统繁忙"),
    UPDATE_ERROR("更新失败"),
    DELETE_ERROR("删除失败"),
    INSERT_ERROR("插入失败");

    private String message;

    public String getMessage() {
        return message;
    }
}
