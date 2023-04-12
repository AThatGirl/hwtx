package com.schedule.service_base.handler.exceptionHandler;



import com.schedule.common.utils.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理类
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResultVO error(Exception e){
        log.error(e.getMessage());
        e.printStackTrace();
        return ResultVO.error();
    }

    @ExceptionHandler(ScheduleException.class)
    @ResponseBody
    public ResultVO error(ScheduleException e){
        log.error(e.getMessage());
        e.printStackTrace();
        return ResultVO.error().message(e.getMsg()).code(e.getCode());
    }
}
