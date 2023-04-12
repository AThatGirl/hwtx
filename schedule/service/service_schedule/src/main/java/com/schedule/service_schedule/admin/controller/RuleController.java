package com.schedule.service_schedule.admin.controller;


import com.schedule.common.utils.ResultVO;
import com.schedule.service_schedule.admin.service.RuleService;
import com.schedule.service_schedule.entity.vo.rule.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tpt
 * @since 2023-02-12
 */
@Slf4j
@RestController
@RequestMapping("/service_schedule/admin/rule")
@Api(tags = "规则接口")
public class RuleController {
    @Resource
    private RuleService ruleService;

    @PostMapping("/insert-store-rule/{id}")
    @ApiOperation(value = "插入门店规则")
    public ResultVO insertScheduleRule(
            @ApiParam(name = "id",value = "门店id",required = true) @PathVariable String id){
        ruleService.insertRule(id);
        return ResultVO.success();
    }

    @GetMapping("/getStoreRules/{storeId}")
    @ApiOperation("获取门店规则")
    public ResultVO getStoreRules(@PathVariable String storeId){
        RuleVo ruleVo=ruleService.getStoreRules(storeId);
        return ResultVO.success().data("rule",ruleVo);
    }

    @PutMapping("/update-store-businessRule")
    @ApiOperation(value = "更新门店营业时间规则")
    public ResultVO updateStoreBusinessRule(
            @ApiParam(name ="businessRule",value = "规则类",required = true)
            @RequestBody BusinessRuleVo businessRule){
        RuleVo ruleVo=new RuleVo();
        ruleVo.setType(businessRule.getType());
        ruleVo.setBusinessRule(businessRule);
        ruleService.update(ruleVo);
        return ResultVO.success();
    }

    @PutMapping("/update-store-endRule")
    @ApiOperation(value = "更新关店规则")
    public ResultVO updateStoreEndRule(
            @ApiParam(name ="endRule",value = "规则类",required = true)
            @RequestBody EndRuleVo endRule){
        RuleVo ruleVo=new RuleVo();
        ruleVo.setType(endRule.getType());
        ruleVo.setEndRule(endRule);
        ruleService.update(ruleVo);
        return ResultVO.success();
    }

    @PutMapping("/update-store-prepareRule")
    @ApiOperation("更新开店准备规则")
    public ResultVO updateStorePrepareRule(@RequestBody PrepareRuleVo prepareRule){
        RuleVo ruleVo=new RuleVo();
        ruleVo.setType(prepareRule.getType());
        ruleVo.setPrepareRule(prepareRule);
        ruleService.update(ruleVo);
        return ResultVO.success();
    }

    @PutMapping("/update-store-mealTimeRule")
    @ApiOperation("更新吃饭时间规则（类型包含了午餐时间规则和晚餐时间规则）")
    public ResultVO updateStoreMealRule(@RequestBody MealTimeRuleVo mealTimeRule){
        RuleVo ruleVo=new RuleVo();
        ruleVo.setType(mealTimeRule.getType());
        if(mealTimeRule.getType().equals("午餐时间规则")){
            ruleVo.setLunchTimeRule(mealTimeRule);
        }else{
            ruleVo.setDinnerTimeRule(mealTimeRule);
        }
        ruleService.update(ruleVo);
        return ResultVO.success();
    }

    @PutMapping("/update-store-noPassengerFlowRule")
    @ApiOperation("更新无客流量规则")
    public ResultVO updateStoreNoPassengerFlowRule(@RequestBody NoPassengerFlowRuleVo noPassengerFlowRule){
        RuleVo ruleVo=new RuleVo();
        ruleVo.setType(noPassengerFlowRule.getType());
        ruleVo.setNoPassengerFlowRule(noPassengerFlowRule);
        ruleService.update(ruleVo);
        return ResultVO.success();
    }

    @PutMapping("/update-store-passengerFlowRule")
    @ApiOperation("更新客流规则")
    public ResultVO updateStorePassengerFlowRule(@RequestBody PassengerFlowRuleVo passengerFlowRule){
        RuleVo ruleVo=new RuleVo();
        ruleVo.setType(passengerFlowRule.getType());
        ruleVo.setPassengerFlowRule(passengerFlowRule);
        ruleService.update(ruleVo);
        return ResultVO.success();
    }

    @PutMapping("/update-store-restTimeRule")
    @ApiOperation("更新休息规则")
    public ResultVO updateStoreRestTimeRule(@RequestBody RestTimeRuleVo restTimeRuleVo){
        RuleVo ruleVo=new RuleVo();
        ruleVo.setType(restTimeRuleVo.getType());
        ruleVo.setRestTimeRule(restTimeRuleVo);
        ruleService.update(ruleVo);
        return ResultVO.success();
    }

    @PutMapping("/update-store-shiftLimitRule")
    @ApiOperation("更新班次限制规则")
    public ResultVO updateStoreShiftLimitRule(@RequestBody ShiftLimitRuleVo shiftLimitRule){
        RuleVo ruleVo=new RuleVo();
        ruleVo.setType(shiftLimitRule.getType());
        ruleVo.setShiftLimitRule(shiftLimitRule);
        ruleService.update(ruleVo);
        return ResultVO.success();
    }

    @PutMapping("/update-store-workHourRule")
    @ApiOperation("更新工作时长规则")
    public ResultVO updateStoreWorkHourRule(@RequestBody WorkHourRuleVo workHourRule){
        RuleVo ruleVo=new RuleVo();
        ruleVo.setType(workHourRule.getType());
        ruleVo.setWorkHourRule(workHourRule);
        ruleService.update(ruleVo);
        return ResultVO.success();
    }




}

