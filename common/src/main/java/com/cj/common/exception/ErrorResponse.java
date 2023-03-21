package com.cj.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一异常处理返回
 */
@Data
@AllArgsConstructor
public class ErrorResponse implements Serializable {
    private String message;
}
