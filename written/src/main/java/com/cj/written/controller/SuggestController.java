package com.cj.written.controller;


import com.cj.common.vo.ResultVO;
import com.cj.written.service.SuggestService;
import com.cj.written.vo.SuggestVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Suggest控制器
 *
 * @author 杰瑞
 * @date 2023/03/07
 */
@RestController
@RequestMapping("/written/suggest")
@Api(tags = "建议", value = "建议")
public class SuggestController {

    @Autowired
    private SuggestService suggestService;


    @GetMapping("/getSuggestById/{id}/{page}")
    @ApiOperation("获取用户建议")
    public ResultVO getSuggestById(@PathVariable String id, @PathVariable String page) {
        return suggestService.getSuggestById(id, page);
    }

    @PostMapping("/submitSuggest")
    @ApiOperation("提交建议")
    public ResultVO submitSuggest(@RequestBody SuggestVO suggestVO) {
        return suggestService.submitSuggest(suggestVO);
    }

    @PostMapping("/deleteSuggestById")
    @ApiOperation("删除建议")
    public ResultVO deleteSuggestById(@RequestBody String[] ids) {
        return suggestService.deleteSuggestById(ids);
    }

    @GetMapping("/getAllSuggest/{storeId}")
    @ApiOperation("获取门店所有建议")
    public ResultVO getAllSuggest(@PathVariable String storeId){
        return suggestService.getAllSuggest(storeId);
    }

}
