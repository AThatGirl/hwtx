package com.cj.common.exception;

import com.cj.common.en.CommonError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClassException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse customException(ClassException e) {

        //记录异常
        log.error("系统异常：{}", e.getMessage(), e);

        //解析异常信息
        String message = e.getMessage();
        return new ErrorResponse(message);

    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse exception(Exception e) {

        //记录异常
        log.error("系统异常{}：", e.getMessage(), e);

        return new ErrorResponse(CommonError.UNKNOW_ERROR.getMessage());

    }


}
