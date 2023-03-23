package com.cj.manage.feign;

import com.cj.common.vo.ResultVO;
import com.cj.common.vo.SendNoticeVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "mq-service", url = "http://localhost:8010/mq-service/notice")
public interface MqServiceFeignClient {

    /**
     * 发送通知消息
     * @param sendNoticeVO 通知参数
     * @return {@link ResultVO}
     */
    @PostMapping("/sendNotice")
    ResultVO sendNotice(@RequestBody SendNoticeVO sendNoticeVO);


}
