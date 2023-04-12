package com.schedule.service_schedule.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.schedule.service_schedule.entity.Rule;
import com.schedule.service_schedule.entity.dto.RunDateTimeDto;
import com.schedule.service_schedule.entity.vo.rule.*;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.util.Json;
import lombok.extern.slf4j.Slf4j;

import java.util.*;


@Slf4j
public class RuleUtil {
    //Logger log= LoggerFactory.getLogger(RuleUtil.class);
    /**
     * 固定规则
     */
    //工作时间值：逗号表示不连续值，-表示连续值 &后面接时间  每一个规则用;隔开    Mon,Wed,  runTime：营业时间
    @ApiModelProperty(value = "门店营业时间规则")
    public static final String runTimeRule="{'runTime':'Mon-Fri&9:00-21:00;Sat-Sun&10:00-22:00'}";

    //weekWorkTime:周工作时间    dayWorkTime：日工作时间   shiftTimeRange：班次时间范围   maxWorkTime：最班次大时间
    @ApiModelProperty(value = "工作时长规则")
    public static final String workTimeRule="{\"weekWorkTime\":\"40\",\"dayWorkTime\":\"8\",\"shiftTimeRange\":\"2-4\",\"maxWorkTime\":\"4\"}";

    //时间范围（如11点到14点，半小时）
    @ApiModelProperty(value = "午餐时间规则")
    public static final String lunchTimeRule="{\"time\":\"11:00-14:00\",\"count\":\"0.5\"}";

    //时间范围（如17点到20点，半小时）
    @ApiModelProperty(value = "晚餐时间规则")
    public static final String dinnerTimeRule="{\"time\":\"17:00-20:00\",\"count\":\"0.5\"}";

    //休息时间规则：时间范围（不限。半小时）
    @ApiModelProperty(value = "休息时间规则")
    public static final String restTimeRule="{\"count\":\"0.5\"}";

    /**
     * 自定义规则
     */
    //关店规则：2,1,13"  公式：门店面积/80 + 1 = 人数
    @ApiModelProperty(value = "关店规则")
    public static final String offRule="{\"after\":\"2\",\"count\":\"1\",\"formula\":\"size/80\"}";

    //客流量规则："3.8"  表示按照业务预测数据，每3.8个客流必须安排至少一个员工当值
    @ApiModelProperty(value = "客流量规则")
    public static final String passengerFlowRule="{\"formula\":\"3.8\"}";

    //开店规则："1.5,23.5" 表示开店1个半小时前需要有员工当值，当值员工数为门店面积除以23.5
    @ApiModelProperty(value = "开店规则")
    public static final String openRule="{\"front\":\"-1\",\"formula\":\"size/100\"}";

    //班次限制规则：每天至少安排三个不超过4小时的班次
    @ApiModelProperty(value = "班次限制规则")
    public static final String shiftLimitRule="{\"count\":\"3\",\"formula\":\"<=4\"}";

    //无客流量规则：如果没有客流量的时候，至少需要1个店员值班.1为缺省值
    @ApiModelProperty(value = "无客流量规则")
    public static final  String noPassengerFlowRule="{\"count\":\"1\"}";

    public static final List<Rule> ruleList=new ArrayList<>();
    static {
        Rule rule=new Rule();
        rule.setType("门店营业时间规则");
        rule.setValue(RuleUtil.runTimeRule);
        ruleList.add(rule);

        rule=new Rule();
        rule.setType("工作时长规则");
        rule.setValue(RuleUtil.workTimeRule);
        ruleList.add(rule);

        rule=new Rule();
        rule.setType("午餐时间规则");
        rule.setValue(RuleUtil.lunchTimeRule);
        ruleList.add(rule);

        rule=new Rule();
        rule.setType("晚餐时间规则");
        rule.setValue(RuleUtil.dinnerTimeRule);
        ruleList.add(rule);

        rule=new Rule();
        rule.setType("休息时间规则");
        rule.setValue(RuleUtil.restTimeRule);
        ruleList.add(rule);

        rule=new Rule();
        rule.setType("关店规则");
        rule.setValue(RuleUtil.offRule);
        ruleList.add(rule);

        rule=new Rule();
        rule.setType("客流量规则");
        rule.setValue(RuleUtil.passengerFlowRule);
        ruleList.add(rule);

        rule=new Rule();
        rule.setType("开店规则");
        rule.setValue(RuleUtil.openRule);
        ruleList.add(rule);

        rule=new Rule();
        rule.setType("班次限制规则");
        rule.setValue(RuleUtil.shiftLimitRule);
        ruleList.add(rule);

        rule=new Rule();
        rule.setType("无客流量规则");
        rule.setValue(RuleUtil.noPassengerFlowRule);
        ruleList.add(rule);
    }
    //处理开店规则和关店规则值
    public static double getOpenAndEndStoreValue(String value){
        value=value.split("/")[1];
        return Double.parseDouble(value);
    }
    //将String转换成具体的星期的门店营业时间规则
    public static List<RunDateTimeDto> getRunTimeValue(String value){
        List<RunDateTimeDto> weekAndTimeList=new ArrayList<>();
        String[] strings=value.split(";");
        log.info(strings[0]);
        for(String str:strings){
            /**
             * {'runTime':'Mou-Fri&9:00-21:00;Sta-Sun&10:00-22:00'}
             */
            String[] strings1=str.split("&");
            //处理星期
            log.error(strings1[0]);
            List<RunDateTimeDto> weekList=WeekUtil.getWeek(strings1[0]);
            weekList.forEach(System.out::println);
            //处理时间
            TimeUtil.setTime(weekList,strings1[1]);
            weekAndTimeList.addAll(weekList);
        }
        //排序按照星期
        Collections.sort(weekAndTimeList);
        return weekAndTimeList;
    }
    //设置rule的值
    public static void setRuleByRuleVo(RuleVo ruleVo,Rule rule){
        Map<String,String> map=null;
        switch (ruleVo.getType()){
            case "门店营业时间规则":
                //处理门店营业时间规则
                rule.setType(ruleVo.getBusinessRule().getType());
                rule.setId(ruleVo.getBusinessRule().getStoreId());
                rule.setValue(WeekUtil.getStringJSONWeek(ruleVo.getBusinessRule().getRunDateTimeDtoList()));
                break;
            case "关店规则":
                rule.setType("关店规则");
                rule.setId(ruleVo.getEndRule().getStoreId());
                map=new HashMap<>();
                map.put("after",""+ruleVo.getEndRule().getAfter());
                map.put("count",""+ruleVo.getEndRule().getCount());
                map.put("formula",ruleVo.getEndRule().getFormula());
                rule.setValue(JSON.toJSONString(map));
                break;
            case "开店规则":
                rule.setType("开店规则");
                rule.setId(ruleVo.getPrepareRule().getStoreId());
                map=new HashMap<>();
                map.put("front",""+ruleVo.getPrepareRule().getFront());
                map.put("formula",ruleVo.getPrepareRule().getFormula());
                rule.setValue(JSON.toJSONString(map));
                break;
            case "工作时长规则":
                rule.setType("工作时长规则");
                rule.setId(ruleVo.getWorkHourRule().getStoreId());
                map=new HashMap<>();
                map.put("weekWorkTime",""+ruleVo.getWorkHourRule().getWeekWorkTime());
                map.put("dayWorkTime",""+ruleVo.getWorkHourRule().getDayWorkTime());
                map.put("shiftTimeRange",""+ruleVo.getWorkHourRule().getShiftTimeRange());
                map.put("maxWorkTime",""+ruleVo.getWorkHourRule().getMaxWorkTime());
                rule.setValue(JSON.toJSONString(map));
                break;
            case "午餐时间规则":
                rule.setType("午餐时间规则");
                rule.setId(ruleVo.getLunchTimeRule().getStoreId());
                map=new HashMap<>();
                map.put("time",ruleVo.getLunchTimeRule().getTime());
                map.put("count",""+ruleVo.getLunchTimeRule().getCount());
                rule.setValue(JSON.toJSONString(map));
                break;
            case "晚餐时间规则":
                rule.setType("晚餐时间规则");
                rule.setId(ruleVo.getDinnerTimeRule().getStoreId());
                map=new HashMap<>();
                map.put("time",ruleVo.getDinnerTimeRule().getTime());
                map.put("count",""+ruleVo.getDinnerTimeRule().getCount());
                rule.setValue(JSON.toJSONString(map));
                break;
            case "休息时间规则":
                rule.setType("休息时间规则");
                rule.setId(ruleVo.getRestTimeRule().getStoreId());
                rule.setValue(JSON.toJSONString(ruleVo.getRestTimeRule()));
                break;
            case "客流量规则":
                rule.setType("客流量规则");
                rule.setId(ruleVo.getNoPassengerFlowRule().getStoreId());
                map=new HashMap<>();
                map.put("formula",ruleVo.getPassengerFlowRule().getFormula());
                rule.setValue(JSON.toJSONString(map));
                break;
            case "班次限制规则":
                rule.setType("班次限制规则");
                rule.setId(ruleVo.getShiftLimitRule().getStoreId());
                map=new HashMap<>();
                map.put("formula",ruleVo.getShiftLimitRule().getFormula());
                map.put("count",""+ruleVo.getShiftLimitRule().getCount());
                rule.setValue(JSON.toJSONString(map));
                break;
            case "无客流量规则":
                rule.setType("无客流量规则");
                rule.setId(ruleVo.getNoPassengerFlowRule().getStoreId());
                map=new HashMap<>();
                map.put("count",""+ruleVo.getNoPassengerFlowRule().getCount());
                rule.setValue(JSON.toJSONString(map));
                break;
        }
    }

    public static void setRuleVoByRule(RuleVo ruleVo,List<Rule> ruleList){
        JSONObject jsonObject=null;
        for(Rule rule:ruleList){
            switch (rule.getType()){
                case "门店营业时间规则":
                    //处理门店营业时间规则
                    BusinessRuleVo businessRuleVo=new BusinessRuleVo();
                    jsonObject=JSON.parseObject(rule.getValue());
                    businessRuleVo.setType(rule.getType());
                    businessRuleVo.setStoreId(rule.getStoreId());
                    businessRuleVo.setRunDateTimeDtoList(RuleUtil.getRunTimeValue(jsonObject.getString("runTime")));
                    ruleVo.setBusinessRule(businessRuleVo);
                    break;
                case "关店规则":
                    EndRuleVo endRuleVo=new EndRuleVo();
                    endRuleVo.setStoreId(rule.getStoreId());
                    endRuleVo.setType(rule.getType());
                    jsonObject=JSON.parseObject(rule.getValue());
                    endRuleVo.setAfter(jsonObject.getDouble("after"));
                    endRuleVo.setCount(jsonObject.getInteger("count"));
                    endRuleVo.setFormula(jsonObject.getString("formula"));
                    ruleVo.setEndRule(endRuleVo);
                    break;
                case "开店规则":
                    PrepareRuleVo prepareRuleVo=new PrepareRuleVo();
                    jsonObject= JSON.parseObject(rule.getValue());
                    prepareRuleVo.setFront(jsonObject.getDouble("front"));
                    prepareRuleVo.setFormula(jsonObject.getString("formula"));
                    prepareRuleVo.setStoreId(rule.getStoreId());
                    prepareRuleVo.setType(rule.getType());
                    ruleVo.setPrepareRule(prepareRuleVo);
                    break;
                case "工作时长规则":
                    WorkHourRuleVo workHourRuleVo=new WorkHourRuleVo();
                    jsonObject=JSON.parseObject(rule.getValue());
                    workHourRuleVo.setStoreId(rule.getStoreId());
                    workHourRuleVo.setType(rule.getType());
                    workHourRuleVo.setDayWorkTime(jsonObject.getDouble("dayWorkTime"));
                    workHourRuleVo.setWeekWorkTime(jsonObject.getDouble("weekWorkTime"));
                    workHourRuleVo.setMaxWorkTime(jsonObject.getDouble("maxWorkTime"));
                    workHourRuleVo.setShiftTimeRange(jsonObject.getString("shiftTimeRange"));
                    ruleVo.setWorkHourRule(workHourRuleVo);
                    break;
                case "午餐时间规则":
                    MealTimeRuleVo mealTimeRuleVo=new MealTimeRuleVo();
                    jsonObject=JSON.parseObject(rule.getValue());
                    mealTimeRuleVo.setType(rule.getType());
                    mealTimeRuleVo.setStoreId(rule.getStoreId());
                    mealTimeRuleVo.setCount(jsonObject.getDouble("count"));
                    mealTimeRuleVo.setTime(jsonObject.getString("time"));
                    ruleVo.setLunchTimeRule(mealTimeRuleVo);
                    break;
                case "晚餐时间规则":
                    MealTimeRuleVo dinnerTimeRuleVo=new MealTimeRuleVo();
                    jsonObject=JSON.parseObject(rule.getValue());
                    dinnerTimeRuleVo.setType(rule.getType());
                    dinnerTimeRuleVo.setStoreId(rule.getStoreId());
                    dinnerTimeRuleVo.setCount(jsonObject.getDouble("count"));
                    dinnerTimeRuleVo.setTime(jsonObject.getString("time"));
                    ruleVo.setDinnerTimeRule(dinnerTimeRuleVo);
                    break;
                case "休息时间规则":
                    RestTimeRuleVo restTimeRuleVo=new RestTimeRuleVo();
                    jsonObject=JSON.parseObject(rule.getValue());
                    restTimeRuleVo.setType(rule.getType());
                    restTimeRuleVo.setCount(jsonObject.getDouble("count"));
                    restTimeRuleVo.setStoreId(rule.getStoreId());
                    ruleVo.setRestTimeRule(restTimeRuleVo);
                    break;
                case "客流量规则":
                    PassengerFlowRuleVo passengerFlowRuleVo=new PassengerFlowRuleVo();
                    jsonObject=JSON.parseObject(rule.getValue());
                    passengerFlowRuleVo.setType(rule.getType());
                    passengerFlowRuleVo.setStoreId(rule.getStoreId());
                    passengerFlowRuleVo.setFormula(jsonObject.getString("formula"));
                    ruleVo.setPassengerFlowRule(passengerFlowRuleVo);
                    break;
                case "班次限制规则":
                    ShiftLimitRuleVo shiftLimitRuleVo=new ShiftLimitRuleVo();
                    jsonObject=JSON.parseObject(rule.getValue());
                    shiftLimitRuleVo.setCount(jsonObject.getInteger("count"));
                    shiftLimitRuleVo.setStoreId(rule.getStoreId());
                    shiftLimitRuleVo.setType(rule.getType());
                    shiftLimitRuleVo.setFormula(jsonObject.getString("formula"));
                    ruleVo.setShiftLimitRule(shiftLimitRuleVo);
                    break;
                case "无客流量规则":
                    NoPassengerFlowRuleVo noPassengerFlowRuleVo=new NoPassengerFlowRuleVo();
                    jsonObject=JSON.parseObject(rule.getValue());
                    noPassengerFlowRuleVo.setCount(jsonObject.getInteger("count"));
                    noPassengerFlowRuleVo.setType(rule.getType());
                    noPassengerFlowRuleVo.setStoreId(rule.getStoreId());
                    ruleVo.setNoPassengerFlowRule(noPassengerFlowRuleVo);
                    break;
            }
        }
    }

}
