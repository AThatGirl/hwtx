package com.schedule.common.utils;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ResultVO {
    @ApiModelProperty(value = "是否成功")
    private Boolean isSuccess;
    @ApiModelProperty(value = "返回码")
    private Integer code;
    @ApiModelProperty(value = "返回消息")
    private String message;

    @ApiModelProperty(value = "返回数据")
    private Map<String, Object> data = new HashMap<String, Object>();
    private ResultVO(){}
    public static ResultVO success(){
        ResultVO r = new ResultVO();
        r.setIsSuccess(true);
        r.setCode(ResultCode.SUCCESS);
        r.setMessage("成功");
        return r;
    }
    public static ResultVO error(){
        ResultVO r = new ResultVO();
        r.setIsSuccess(false);
        r.setCode(ResultCode.ERROR);
        r.setMessage("失败");
        return r;
    }
    public ResultVO isSuccess(Boolean success){
        this.setIsSuccess(success);
        return this;
    }
    public ResultVO message(String message){
        this.setMessage(message);
        return this;
    }
    public ResultVO code(Integer code){
        this.setCode(code);
        return this;
    }
    public ResultVO data(String key, Object value){
        this.data.put(key, value);
        return this;
    }
    public ResultVO data(Map<String, Object> map){
        this.setData(map);
        return this;
    }
}
