package com.cj.mq_service.controller;

import com.cj.common.vo.ResultVO;
import com.cj.mq_service.service.StoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mq-service/store")
@Api(tags = "门店消息")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @GetMapping("/updateStore/{msg}")
    @ApiOperation("通知门店内容更新")
    public ResultVO updateStore(@PathVariable("msg") String msg){
        //发送消息，让门店更新人数
        storeService.updateStore(msg);
        return ResultVO.success();
    }


}
