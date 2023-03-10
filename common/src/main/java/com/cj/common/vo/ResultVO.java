package com.cj.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 返回结果对象
 *
 * @author 杰瑞
 * @date 2022/10/09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResultVO {

    public static final String SUCCESS_CODE = "success";
    public static final String FILE_CODE = "fail";

    //响应状态码
    private String code;
    //响应信息
    private String message;
    //响应数据
    private Object data;

    //成功的方法
    public static ResultVO success(){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(SUCCESS_CODE);
        return resultVO;
    }
    //失败的方法
    public static ResultVO fail(){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(FILE_CODE);
        return resultVO;
    }

    public ResultVO setMessage(String message){
        this.message = message;
        return this;
    }
    public ResultVO setData(Object data){
        this.data = data;
        return this;
    }


}
