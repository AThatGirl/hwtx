package com.cj.manage.feign;

import com.cj.common.vo.ResultVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "mq-service", url = "http://localhost:8010/mq-service/store")
public interface MqServiceFeignClient {


    @GetMapping("/updateStore/{msg}")
    @ApiOperation("通知门店内容更新")
    ResultVO updateStore(@PathVariable("msg") String msg);



}
