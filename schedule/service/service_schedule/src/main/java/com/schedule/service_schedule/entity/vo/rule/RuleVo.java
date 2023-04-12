package com.schedule.service_schedule.entity.vo.rule;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class RuleVo implements Serializable {
    private String StoreId;
    private String type;
    @ApiModelProperty("门店营业时间规则")
    private BusinessRuleVo businessRule;
    @ApiModelProperty("关店规则")
    private EndRuleVo endRule;
    @ApiModelProperty("午饭时间规则")
    private MealTimeRuleVo lunchTimeRule;
    @ApiModelProperty("晚饭时间规则")
    private MealTimeRuleVo dinnerTimeRule;
    @ApiModelProperty("无客流量规则")
    private NoPassengerFlowRuleVo noPassengerFlowRule;
    @ApiModelProperty("开店规则")
    private PrepareRuleVo prepareRule;
    @ApiModelProperty("休息时间规则")
    private RestTimeRuleVo restTimeRule;
    @ApiModelProperty(value = "班次限制规则")
    private ShiftLimitRuleVo shiftLimitRule;
    @ApiModelProperty(value = "工作时长规则")
    private WorkHourRuleVo workHourRule;
    @ApiModelProperty("客流量规则")
    private PassengerFlowRuleVo passengerFlowRule;
}
