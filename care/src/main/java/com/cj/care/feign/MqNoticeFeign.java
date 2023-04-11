package com.cj.care.feign;

import com.cj.common.vo.ResultVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "mq-service", url = "http://localhost:8010/mq-service/notice")
public interface MqNoticeFeign {

    @PostMapping("/sendBlessing")
    @ApiOperation("发送祝福语")
    ResultVO sendBlessing(String msg);

}
