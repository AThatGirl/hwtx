package com.cj.common.utils;

import com.cj.common.entity.User;
import com.cj.common.vo.ResultVO;

/**
 * 检查
 *
 * @author 杰瑞
 * @date 2023/03/05
 */
public class CheckUtils {


    public static boolean checkPass(Integer pass, ResultVO resultVO) {
        //查看是否通过审核
        if (pass.equals(User.NO_PASS)) {
            resultVO.setCode(ResultVO.FILE_CODE);
            resultVO.setMessage("审核不通过");
            return false;
        }else if (pass.equals(User.PASSING)){
            resultVO.setData(ResultVO.FILE_CODE);
            resultVO.setMessage("审核中");
            return false;
        }else {
            return true;
        }
    }

}
