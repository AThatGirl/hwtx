package com.cj.manage.controller;

import com.cj.common.entity.Store;
import com.cj.common.vo.ResultVO;
import com.cj.manage.service.StoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manage/store")
@Api(tags = "门店管理", value = "门店管理")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @GetMapping("/search/{id}")
    @ApiOperation("查询门店信息")
    public ResultVO search(@PathVariable("id") String id){
        return storeService.search(id);
    }

    @PostMapping("/changeStoreInfo")
    @ApiOperation("修改门店信息")
    public ResultVO changeStoreInfo(@RequestBody Store store){
        return storeService.changeStoreInfo(store);
    }

    // TODO 放行
    @GetMapping("/searchAllStore")
    @ApiOperation("查询所有门店")
    public ResultVO searchAllStore(){
        return storeService.searchAllStore();
    }

}
