package com.cj.manage.controller;

import com.cj.common.entity.Store;
import com.cj.common.vo.ResultVO;
import com.cj.manage.service.StoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/manage/store")
@Api(tags = "门店管理", value = "门店管理")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @GetMapping("/search/{id}")
    @ApiOperation("查询门店信息")
    public ResultVO search(@RequestParam String id){
        return storeService.search(id);
    }

    @PostMapping("/changeStoreInfo")
    @ApiOperation("修改门店信息")
    public ResultVO changeStoreInfo(@RequestBody Store store){
        return storeService.changeStoreInfo(store);
    }


}