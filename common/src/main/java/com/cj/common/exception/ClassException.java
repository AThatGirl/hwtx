package com.cj.common.exception;

import com.cj.common.en.CommonError;

/**
 * 统一异常
 */
public class ClassException extends RuntimeException{

    public ClassException(String message) {
        super(message);
    }

    public static void cast(String message){
        throw new ClassException(message);
    }
    public static void cast(CommonError commonError){
        throw new ClassException(commonError.getMessage());
    }

}
