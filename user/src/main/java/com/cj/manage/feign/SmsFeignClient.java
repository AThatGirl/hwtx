package com.cj.manage.feign;

import com.cj.common.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 远程调用sms模块
 *
 * @author 杰瑞
 * @date 2023/02/23
 */
@FeignClient(value = "sms", url = "http://localhost:8005/sms/sms")
public interface SmsFeignClient {

    /**
     * 通过电话得到验证码
     *
     * @param phone 电话
     * @return {@link ResultVO}
     */
    @GetMapping("/getNoteByPhone/{phone}")
    ResultVO getNoteByPhone(@PathVariable("phone") String phone);

}
