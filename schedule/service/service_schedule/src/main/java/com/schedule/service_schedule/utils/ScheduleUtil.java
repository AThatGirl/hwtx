package com.schedule.service_schedule.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.schedule.service_base.handler.exceptionHandler.ScheduleException;
import com.schedule.service_schedule.entity.dto.WorkFormDto;
import com.schedule.service_schedule.entity.dto.*;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.*;
@Slf4j
public class ScheduleUtil {
    /**
     * 1.必须给工作时间完全覆盖午餐、晚餐时间的员工，安排午餐或晚餐时间
     * 2.每天开店之前需要1小时做准备工作（如做清洁）。1为缺省值，可调整
     *      公式：门店面积/100 = 人数。100为缺省值，可调整
     * 3.班次生成公式：预测客流/3.8 = 店员需求数。3.8为缺省值，可调整
     * 4.每天关店之后需要2小时做收尾工作（如盘点、清算、清洁）。2为缺省值。可调整
     *      公式：门店面积/80 + 1 = 人数。80和1为缺省值。可调整
     * 5.用户可以设置允许执行此类工作的职位。可以设置为所有职位，也可以限制特定职位（比如导购人员、收银，店经理等）
     */

    /**
     * 生成一个星期的班次,通过班次人数，门店规则（固定规则：门店营业时间规则,自定义规则：）
     * @param shiftVoLists
     * @param ruleMap 规则集合包含了json的String类型值  门店营业时间规则
     * @return
     */
    public static List<List<WorkFormDto>> GenerateShifts(List<List<ShiftDto>> shiftVoLists, Map<String,String> ruleMap){
        List<List<WorkFormDto>> weeklists=new ArrayList<>();//存储一个星期的班次
        List<WorkFormDto> workFormDtoList =null;//存储某天生成的班次
        /**
         * 班次限制规则：{"count":"3","formula":"<=4"}
         * "工作时长规则"：{"weekWorkTime":"40","dayWorkTime":"8","shiftTimeRange":"2-4","maxShiftTime":"4"}
         */
        //1.获取规则json值
        JSONObject shiftLimitJSON;
        JSONObject workTimeJSON;
        try{
            shiftLimitJSON = JSON.parseObject(ruleMap.get("班次限制规则"));
            workTimeJSON=JSON.parseObject(ruleMap.get("工作时长规则"));
        }catch (Exception e){
            throw new ScheduleException(20001,"班次生成获取规则值失败:"+e.getMessage());
        }
        //获取规则信息
        String[] shiftTimeRange=workTimeJSON.getString("shiftTimeRange").split("-");
        double maxShiftTimeRange=Double.parseDouble(shiftTimeRange[1]);
        double minShiftTimeRange=Double.parseDouble(shiftTimeRange[0]);
        double maxShiftTime=maxShiftTimeRange;
        double shiftLimit=Double.parseDouble(shiftLimitJSON.getString("formula").split("<=")[1]);
        Integer shiftLimitCount=shiftLimitJSON.getInteger("count");
        //3.按照门店营业时间生成一个星期的班次
        for(int i=0;i<shiftVoLists.size();i++){
            //每天班次需要人数的集合
            List<ShiftDto> shiftDtoList =shiftVoLists.get(i);
            /**
             *  根据班次人数集合生成排班表
             *  1.判断开店班次是否超过了最大班次时间限制
             *  2.开始生成其他班次
             *  3.判断关店班次是否超过最大班次时间限制
             */

            workFormDtoList =new ArrayList<>();//用于存放当前生成的班次
            List<List<WorkFormDto>> sortLists=new ArrayList<>();//用于分类生成的班次
            //生成开店班次
            //用于存储开店班次的时长
            double startShiftTime=TimeUtil.calculateStringTimeSub(shiftDtoList.get(0).getEndTime(), shiftDtoList.get(0).getStartTime());
            getShift(sortLists, shiftDtoList.get(0),maxShiftTime,startShiftTime);
            //循环生成客流量班次,除了开店班次和关店班次
            for(int a = 1; a< shiftDtoList.size()-1; a++){
                ShiftDto shiftDto = shiftDtoList.get(a);
                getShift(sortLists, shiftDto,maxShiftTime,1);
            }
            //生成关店班次
            getShift(sortLists, shiftDtoList.get(shiftDtoList.size()-1),maxShiftTime, shiftDtoList.get(shiftDtoList.size()-1).getShiftTime());

            //处理班次时间不符合规则的班次,判断是否有和当前班次start时间一样的end时间的班次
            dealShiftRangeRule(sortLists,minShiftTimeRange);

            //进行班次限制规则的判定
            dealShiftLimitRule(sortLists,shiftLimit,shiftLimitCount,minShiftTimeRange);

            //把lists里面的全部拿出来
            for(List<WorkFormDto> list:sortLists){
                for(WorkFormDto workFormDto :list){
                    workFormDtoList.add(workFormDto);
                }
            }
            weeklists.add(workFormDtoList);

        }


        return weeklists;
    }
    //处理小于规定的班次范围的班次
    private static void dealShiftRangeRule(List<List<WorkFormDto>> sortLists,double minShiftTimeRange) {

        for(int index=0;index<sortLists.size();index++){
            List<WorkFormDto> list=sortLists.get(index);
            WorkFormDto dealWorkFormDto =list.get(0);
            if(dealWorkFormDto.getShiftTime()<minShiftTimeRange){
                int needDealShiftNum=list.size();//需要处理的班次个数
                double shiftNeedTime=minShiftTimeRange- dealWorkFormDto.getShiftTime();//班次需要增加的时间
                String dealWorkFormStartTime= dealWorkFormDto.getStartTime();//要处理的班次开始时间，应为后面修改了，需要保存不能用前面的
                String dealWorkFormEndTime= dealWorkFormDto.getEndTime();//结束时间
                List<WorkFormDto> dealAfterWorkFormDtoListFront =new ArrayList<>();//往前找的，处理过后的班次到最后再进行判断插入情况
                List<WorkFormDto> dealAfterWorkFormDtoListAfter =new ArrayList<>();//往后找的
                if(index!=0){
                    //往前找
                    for(int a=index-1;a>=0;a--){
                        List<WorkFormDto> findList=sortLists.get(a);
                        if(TimeUtil.compareToStringTime(findList.get(0).getEndTime(),dealWorkFormStartTime)==0){
                            //判断是否可以减少时间
                            if(findList.get(0).getShiftTime()-shiftNeedTime>=minShiftTimeRange){
                                //比较大小，处理不同情况,大于需要拿出来一部分
                                if(findList.size()>needDealShiftNum){
                                    List<WorkFormDto> dealFindList=new ArrayList<>();//被删减过后的班次
                                    for(int b=0;b<needDealShiftNum;b++){
                                        WorkFormDto findWorkFormDto =findList.remove(b);
                                        findWorkFormDto.setEndTime(TimeUtil.CalculateTime(findWorkFormDto.getEndTime(),-shiftNeedTime));
                                        findWorkFormDto.setShiftTime(findWorkFormDto.getShiftTime()-shiftNeedTime);
                                        dealFindList.add(findWorkFormDto);
                                    }
                                    //将数据插入回去
                                    sortLists.add(a,dealFindList);

                                    needDealShiftNum=0;
                                    //给需要修改的班次加上班次需要增加的时间
                                    for(int b=0;b<list.size();b++){
                                        WorkFormDto workFormDto =list.remove(b);
                                        workFormDto.setStartTime(TimeUtil.CalculateTime(workFormDto.getStartTime(),-shiftNeedTime));
                                        workFormDto.setShiftTime(workFormDto.getShiftTime()+shiftNeedTime);
                                        dealAfterWorkFormDtoListFront.add(workFormDto);
                                    }
                                }
                                else {
                                    //小于等于时
                                    for(WorkFormDto workFormDto :findList){
                                        workFormDto.setEndTime(TimeUtil.CalculateTime(workFormDto.getEndTime(),-shiftNeedTime));
                                        workFormDto.setShiftTime(workFormDto.getShiftTime()-shiftNeedTime);
                                    }
                                    needDealShiftNum=needDealShiftNum-findList.size();
                                    for(int b=0;b<findList.size();b++){
                                        WorkFormDto workFormDto =list.get(b);
                                        workFormDto.setStartTime(TimeUtil.CalculateTime(workFormDto.getStartTime(),-shiftNeedTime));
                                        workFormDto.setShiftTime(workFormDto.getShiftTime()+shiftNeedTime);
                                        dealAfterWorkFormDtoListFront.add(workFormDto);
                                    }
                                }

                            }

                        }
                        if(needDealShiftNum==0){
                            break;
                        }
                    }
                }

                if(index!=sortLists.size()-1&&needDealShiftNum!=0){
                    //往后找
                    for(int a=index+1;a<sortLists.size();a++){
                        List<WorkFormDto> findList=sortLists.get(a);
                        if(TimeUtil.compareToStringTime(findList.get(0).getStartTime(),dealWorkFormEndTime)==0){
                            //判断是否可以减少时间
                            if(findList.get(0).getShiftTime()-shiftNeedTime>=minShiftTimeRange){
                                //比较大小，处理不同情况
                                if(findList.size()>needDealShiftNum){
                                    List<WorkFormDto> dealFindList=new ArrayList<>();//被删减过后的班次
                                    for(int b=0;b<needDealShiftNum;b++){
                                        WorkFormDto findWorkFormDto =findList.remove(b);
                                        findWorkFormDto.setStartTime(TimeUtil.CalculateTime(findWorkFormDto.getStartTime(),shiftNeedTime));
                                        findWorkFormDto.setShiftTime(findWorkFormDto.getShiftTime()-shiftNeedTime);
                                        dealFindList.add(findWorkFormDto);
                                    }
                                    //将数据插入回去
                                    sortLists.add(a,dealFindList);

                                    needDealShiftNum=0;
                                    //给需要修改的班次加上班次需要增加的时间
                                    for(WorkFormDto workFormDto :list){
                                        list.remove(workFormDto);
                                        workFormDto.setEndTime(TimeUtil.CalculateTime(workFormDto.getEndTime(),shiftNeedTime));
                                        workFormDto.setShiftTime(workFormDto.getShiftTime()+shiftNeedTime);
                                        dealAfterWorkFormDtoListAfter.add(workFormDto);
                                    }
                                }
                                else {
                                    //小于等于时
                                    for(WorkFormDto workFormDto :findList){
                                        workFormDto.setStartTime(TimeUtil.CalculateTime(workFormDto.getStartTime(),shiftNeedTime));
                                        workFormDto.setShiftTime(workFormDto.getShiftTime()-shiftNeedTime);
                                    }
                                    needDealShiftNum=needDealShiftNum-findList.size();
                                    for(int b=0;b<findList.size();b++){
                                        WorkFormDto workFormDto =list.get(b);
                                        workFormDto.setEndTime(TimeUtil.CalculateTime(workFormDto.getEndTime(),shiftNeedTime));
                                        workFormDto.setShiftTime(workFormDto.getShiftTime()+shiftNeedTime);
                                        dealAfterWorkFormDtoListAfter.add(workFormDto);
                                    }
                                }

                            }

                        }
                        if(needDealShiftNum==0){
                            break;
                        }
                    }
                }
                //处理两种情况 list是否为空
                if(dealAfterWorkFormDtoListFront.size()!=0){
                    if(list.size()!=0){
                        sortLists.add(0, dealAfterWorkFormDtoListFront);
                        index+=1;
                    }else {
                        list.addAll(dealAfterWorkFormDtoListFront);
                    }
                }
                if(dealAfterWorkFormDtoListAfter.size()!=0){
                    if(list.size()!=0){
                        sortLists.add(0, dealAfterWorkFormDtoListAfter);
                        index+=1;
                    }else {
                        list.addAll(dealAfterWorkFormDtoListFront);
                    }
                }
            }
        }
    }

    //处理班次限制要求
    private static void dealShiftLimitRule(List<List<WorkFormDto>> sortLists,double shiftLimit,int shiftLimitCount,double minShiftTimeRange) {
        //统计符合的班次
        int countShift=0;
        for(List<WorkFormDto> list:sortLists){
            WorkFormDto workFormDto=list.get(0);
            if(workFormDto.getShiftTime()<=shiftLimit){
                countShift+=1;
            }
            if(countShift>=shiftLimitCount){
                break;
            }
        }
        //判断是否符合规则
        if(countShift<shiftLimitCount){
            //不符合进行处理
            int needDealShiftNum=shiftLimitCount-countShift;//还需要的班次个数
            for(int sortListsIndex=0;sortListsIndex<sortLists.size();sortListsIndex++){
                List<WorkFormDto> list=sortLists.get(sortListsIndex);
                for(int listIndex=0;listIndex<list.size();listIndex++){
                    WorkFormDto dealWorkFormDto=list.get(listIndex);
                    double flag=dealWorkFormDto.getShiftTime()/minShiftTimeRange;//查看能够分最大的个数
                    if(flag>=2){//如果不大于2的话会有不符合班次最小的时间，默认shiftLimit是满足不会小于班次最小的时间
                        //直接将班次划分一个出来
                        String createShiftStartTime=dealWorkFormDto.getStartTime();
                        //先修改原班次
                        dealWorkFormDto.setStartTime(TimeUtil.CalculateTime(dealWorkFormDto.getStartTime(),minShiftTimeRange));
                        dealWorkFormDto.setShiftTime(dealWorkFormDto.getShiftTime()-minShiftTimeRange);
                        //生成一个新的班次
                        WorkFormDto createWorkFormDto=new WorkFormDto();
                        BeanUtils.copyProperties(dealWorkFormDto,createWorkFormDto);
                        createWorkFormDto.setShiftTime(minShiftTimeRange);
                        createWorkFormDto.setEndTime(dealWorkFormDto.getStartTime());
                        createWorkFormDto.setStartTime(createShiftStartTime);
                        //添加到dealWorkFormDto的位置之前
                        list.add(listIndex,createWorkFormDto);
                        needDealShiftNum-=1;
                        //判断被分过后是否也符合要求
                        if(dealWorkFormDto.getShiftTime()<=shiftLimit){
                            needDealShiftNum-=1;
                        }
                        if (needDealShiftNum<=0){
                            break;
                        }
                    }
                }
                if(needDealShiftNum<=0){
                    break;
                }
            }
        }
    }

    /**
     * 通过客流量数据,客流量规则和门店面积生成班次人数
     * @param  passengerFlowExcelVoLists 客流量数据
     * @param ruleMap 规则map集合：例如：开店规则：{front:1}
     * @return List<PositionVo> 班次所需人数集合
     */
    public static List<List<ShiftDto>> getShiftsPeopleNum(List<List<PassengerFlowExcelDto>> passengerFlowExcelVoLists, Map<String,String> ruleMap, double storeArea){
        List<ShiftDto> passengerFlowShiftDtoList = null;//用于存放客流量班次人数
        List<List<ShiftDto>> passengerFlowShiftVoLists=new ArrayList<>();//用于存放所有的客流量班次人数
        List<ShiftDto> shiftDtoList =null;
        List<List<ShiftDto>> shiftVoLists=new ArrayList<>();
        /**
         *  客流量规则："3.8"
         *  {"formula":"3.8"}
         *  无客流量规则:{"count":"1"}
         *  开店规则:
         *  关店规则:
         *  门店营业时间规则:
         */
        //1.获取规则的值
        JSONObject noPassengerFlowJSON;
        JSONObject passengerFlowJSON;
        JSONObject storeRunTimeJSON;
        JSONObject startJSON;
        JSONObject endJSON;
        try{
            startJSON = JSON.parseObject(ruleMap.get("开店规则"));
            endJSON = JSON.parseObject(ruleMap.get("关店规则"));
            storeRunTimeJSON = JSON.parseObject(ruleMap.get("门店营业时间规则"));
            passengerFlowJSON = JSON.parseObject(ruleMap.get("客流量规则"));
            noPassengerFlowJSON = JSON.parseObject(ruleMap.get("无客流量规则"));
        }catch (Exception e){
            throw new ScheduleException(20001,"客流量生成获取规则失败:"+e.getMessage());
        }
        //无客流量规则值，用于判定是否为0时，赋值为无客流量规则值
        Integer noPassengerFlow=noPassengerFlowJSON.getInteger("count");
        //获取门店营业时间
        List<RunDateTimeDto> runDateTimeDtoList =RuleUtil.getRunTimeValue(storeRunTimeJSON.getString("runTime"));
        runDateTimeDtoList.forEach(r->{
            log.info(r.toString());
        });
        //判断是否有足够的客流量数据
        if(passengerFlowExcelVoLists.size()!= runDateTimeDtoList.size()){
            throw new ScheduleException(20001,"客流量数据和门店营业时间不匹配！");
        }
        //获取开店班次规则信息
        int startShiftNum=(int)(storeArea/RuleUtil.getOpenAndEndStoreValue((String) startJSON.get("formula")));
        double startShiftFrontTime=startJSON.getDouble("front");
        //获取关店班次规则信息
        int endShiftNum=(int)(storeArea/RuleUtil.getOpenAndEndStoreValue(endJSON.getString("formula")))+endJSON.getInteger("count");
        double endShiftAfterTime=endJSON.getDouble("after");
        //获取客流量班次需要人数
        for(List<PassengerFlowExcelDto> passengerFlowExcelDtoList :passengerFlowExcelVoLists){
            passengerFlowShiftDtoList =new ArrayList<>();
            for(int i = 0; i< passengerFlowExcelDtoList.size(); i+=2){
                //3.获取客流量班次人数
                ShiftDto shiftDto =new ShiftDto();
                //计算需要人数  向上取整  Math.ceil()向上取整
                Double passengerFlow = passengerFlowJSON.getDouble("formula");
                double num=Math.ceil(passengerFlowExcelDtoList.get(i).getNumPeople()/passengerFlow) +
                        Math.ceil(passengerFlowExcelDtoList.get(i + 1).getNumPeople()/passengerFlow);
                num=Math.ceil(num/2.0);
                //将属性赋值到类中
                //判断是否没有客流量
                if(num==0){
                    shiftDto.setPeopleNum(noPassengerFlow);
                }else{
                    shiftDto.setPeopleNum((int)num);
                }
                shiftDto.setStartTime(passengerFlowExcelDtoList.get(i).getStartTime());
                shiftDto.setEndTime(passengerFlowExcelDtoList.get(i+1).getEndTime());
                shiftDto.setDate(passengerFlowExcelDtoList.get(i).getDate());
                passengerFlowShiftDtoList.add(shiftDto);
            }
            passengerFlowShiftVoLists.add(passengerFlowShiftDtoList);
        }

        //生成所有的班次通过客流量班次（包含开店班次和关店班次）
        for(int i = 0; i< runDateTimeDtoList.size(); i++){
            RunDateTimeDto runDateTimeDto = runDateTimeDtoList.get(i);
            shiftDtoList =new ArrayList<>();
            //找到需要生成班次的客流量数据，就是星期
            for(List<ShiftDto> list:passengerFlowShiftVoLists){
                //比较前三个字母，不区分大小写
                org.joda.time.LocalDate localDate= org.joda.time.LocalDate.parse(DateUtil.dealDateFormat(list.get(0).getDate()));
                String week= java.time.LocalDate.parse(localDate.toString()).getDayOfWeek().toString().substring(0,3);
                if(runDateTimeDto.getWeek().compareToIgnoreCase(week)==0){
                    passengerFlowShiftDtoList =list;
                    break;
                }
            }
            //生成开店班次
            ShiftDto startShift=new ShiftDto();
            startShift.setPeopleNum(startShiftNum);
            startShift.setStartTime(TimeUtil.CalculateTime(runDateTimeDto.getStartTime(),startShiftFrontTime));
            startShift.setEndTime(runDateTimeDto.getStartTime());
            startShift.setDate(passengerFlowShiftDtoList.get(0).getDate());
            startShift.setShiftTime(-startShiftFrontTime);
            shiftDtoList.add(startShift);
            //生成客流量班次,通过门店营业时间的开始和结束生成
            for (int a = 0; TimeUtil.compareToStringTime(TimeUtil.CalculateTime(runDateTimeDto.getStartTime(), a), runDateTimeDto.getEndTime())==-1; a++){
                //要生成的时间
                String needCreateShiftOfTime=TimeUtil.CalculateTime(runDateTimeDto.getStartTime(), a);
                boolean flag=false;
                //去passengerFlowShiftVoList里面找
                for(ShiftDto shiftDto : passengerFlowShiftDtoList){
                    //判断是否有这个时间段的客流量数据
                    int startFlag=TimeUtil.compareToStringTime(needCreateShiftOfTime, shiftDto.getStartTime());
                    int endFlag=TimeUtil.compareToStringTime(TimeUtil.CalculateTime(needCreateShiftOfTime,1), shiftDto.getEndTime());
                    if(startFlag==0&&endFlag==0) {
                        //有直接使用
                        shiftDtoList.add(shiftDto);
                        flag=true;
                        break;
                    }
                    if(TimeUtil.compareToStringTime(shiftDto.getStartTime(),needCreateShiftOfTime)==1){
                        break;
                    }
                }
                if (!flag){
                    //没有，生成
                    ShiftDto createShiftDto =new ShiftDto();
                    createShiftDto.setStartTime(needCreateShiftOfTime);
                    createShiftDto.setPeopleNum(noPassengerFlow);
                    createShiftDto.setEndTime(TimeUtil.CalculateTime(needCreateShiftOfTime,1));
                    createShiftDto.setDate(passengerFlowShiftDtoList.get(0).getDate());
                    shiftDtoList.add(createShiftDto);
                }

            }
            //生成关店班次
            ShiftDto endShiftDto =new ShiftDto();
            endShiftDto.setPeopleNum(endShiftNum);
            endShiftDto.setStartTime(runDateTimeDto.getEndTime());
            endShiftDto.setEndTime(TimeUtil.CalculateTime(runDateTimeDto.getEndTime(),endShiftAfterTime));
            endShiftDto.setDate(passengerFlowShiftDtoList.get(0).getDate());
            endShiftDto.setShiftTime(endShiftAfterTime);
            shiftDtoList.add(endShiftDto);

            //添加进去
            shiftVoLists.add(shiftDtoList);
        }
        return shiftVoLists;
    }

    /**
     * 排班算法：使用优先级，对班次符合人数进行统计，然后再升序对班次进行人员安排，以及条件判定（职位，日工作时间是否超过，总工作时间是否超过）
     * @param shiftsList   七天班次集合,按开始时间和结束时间升序
     * @param employeeDtoList  员工集合
     * @param ruleMap     规则集合
     * @return
     */
    public static void scheduling(List<List<List<WorkFormDto>>> shiftsList, List<EmployeeDto> employeeDtoList, Map<String,String> ruleMap){
        /**
         * 工作时长规则: {"weekWorkTime":"40","dayWorkTime":"8","shiftTimeRange":"2-4","maxWorkTime":"4"}
         * 午餐时间规则: {"time":"11:00-14:00","count":"0.5"}
         * 晚餐时间规则: {"time":"17:00-20:00","count":"0.5"}
         * 休息时间规则: {"count":"0.5"}
         */
        //用来统计每一个班次满足人数
        List<ShiftSortDto> shiftSortDtoList=new ArrayList<>();

        //对班次进行人数统计和排序
        totalShiftPeople(shiftSortDtoList, employeeDtoList,shiftsList);
        //循环排班
        for(;shiftSortDtoList.size()>0;){
            //升序排序shiftSortList
            Collections.sort(shiftSortDtoList);
            //选取优先的班次排班
            ShiftSortDto scheduleShiftSortDto=scheduleEmployee(shiftSortDtoList,employeeDtoList);
            //处理剩下的班次，把不符合的员工删除
            dealShift(shiftSortDtoList,scheduleShiftSortDto,ruleMap);
        }
    }

    //将员工安排到班次中，将排的班次和员工返回
    private static ShiftSortDto scheduleEmployee(List<ShiftSortDto> shiftSortDtoList,List<EmployeeDto> employeeDtoList) {
        //用来存放与第一个班次相同员工数量的班次集合
        List<ShiftSortDto> sameShiftSortDtoList=new ArrayList<>();
        //待选取的员工集合
        List<EmployeeDto> willSelectedEmployeeDtoList=new ArrayList<>();
        //排班的员工
        EmployeeDto scheduledEmployeeDto=null;
        //要排的班次
        WorkFormDto scheduleWorkFormDto=null;
        //返回的班次和员工
        ShiftSortDto returnShiftSortDto=new ShiftSortDto();
        /**
         *  统计一下员工id数量出现，选择最低的那个，如果有多个判断插入过后会有几个班次被影响，选最少的，如果还有相同的选周时间少的
         *
         */
        //获取第一个排班班次
        ShiftSortDto firstShiftSortDto =shiftSortDtoList.get(0);

        //获取相同员工数量的班次
        for(ShiftSortDto shiftSortDto:shiftSortDtoList){
            if(shiftSortDto.getList().size()==firstShiftSortDto.getList().size()){
                sameShiftSortDtoList.add(shiftSortDto);
            }else{
                break;
            }
        }
        //判断是否有超过了两个相同班次
        if(sameShiftSortDtoList.size()>2){
            /**
             * 统计一下员工id数量出现，选择最低的那个
             * 如果有多个选择，比较 日排班时间少，日时间相同周时间少的，没有特殊情况的比如孕妇等
             */
            //员工出现的次数集合
            Map<String,Integer> employeeCountMap=new HashMap<>();
            List<String> employeeIdList=new ArrayList<>();
            //统计员工出现次数
            for(ShiftSortDto shiftSortDto:sameShiftSortDtoList){
                for (EmployeeDto employeeDto:shiftSortDto.getList()){
                    //员工id不存在，添加进去
                    if(!employeeCountMap.containsKey(employeeDto.getId())){
                        employeeCountMap.put(employeeDto.getId(),1);
                    }else{
                        //包含增加
                        employeeCountMap.replace(employeeDto.getId(),employeeCountMap.get(employeeDto.getId())+1);
                    }
                }
            }
            //排序
            List<Map.Entry<String, Integer>> list = new ArrayList<>(employeeCountMap.entrySet()); //转换为list
            list.sort(Map.Entry.comparingByValue());
            //获取和第一个相同出现次数的员工id
            int flag=list.get(0).getValue();
            for(Map.Entry<String, Integer> map:list){
                if(flag==map.getValue()){
                    employeeIdList.add(map.getKey());
                }else{
                    break;
                }
            }
            //获取待选员工
            for(String s:employeeIdList){
                for(EmployeeDto employeeDto:employeeDtoList){
                    if(s.equals(employeeDto.getId())){
                        willSelectedEmployeeDtoList.add(employeeDto);
                    }
                }
            }
            //选取一个周时间最少的
            scheduledEmployeeDto=willSelectedEmployeeDtoList.get(0);
            for(int i=1;i<willSelectedEmployeeDtoList.size();i++){
                EmployeeDto employeeDto=employeeDtoList.get(i);
                if(scheduledEmployeeDto.getWeekWorkTime()>employeeDto.getWeekWorkTime()){
                    scheduledEmployeeDto=employeeDto;
                }else if(scheduledEmployeeDto.getWeekWorkTime()==employeeDto.getWeekWorkTime()){
                    //比较特殊情况
                }
            }
            //获取包含这个员工的班次
            ShiftSortDto scheduleShiftSortDto=sameShiftSortDtoList.get(0);
            for(int i=0;i<sameShiftSortDtoList.size();i++){
                ShiftSortDto shiftSortDto=sameShiftSortDtoList.get(i);
                if(shiftSortDto.getList().contains(scheduledEmployeeDto)){
                    scheduleShiftSortDto=shiftSortDto;
                    break;
                }
            }
            scheduleWorkFormDto=scheduleShiftSortDto.getWorkFormDtoList().get(0);
            //安排班次
            scheduleShift(scheduleShiftSortDto,scheduledEmployeeDto);
        }else{
            //只有一个相同班次时
            //获取要排的班次
            ShiftSortDto scheduleShiftSortDto=sameShiftSortDtoList.get(0);
            //用来判断星期几
            WorkFormDto workFormDto = scheduleShiftSortDto.getWorkFormDtoList().get(0);
            //用于判断星期几
            String week= WeekUtil.dealDateToStringWeek(workFormDto.getDate());
            int weekFlag=WeekUtil.Week.indexOf(week);
            //获取里面的员工按日工作时间最短的员工，如果如果日工作时间相同比较周工作时间
            List<EmployeeDto> employeeDtos = sameShiftSortDtoList.get(0).getList();
            scheduledEmployeeDto = employeeDtos.get(0);
            for(int a = 1; a< employeeDtos.size(); a++){
                EmployeeDto employeeDto = employeeDtos.get(a);
                double minEmployeeDayWorkTime= scheduledEmployeeDto.getDayWorkTime().get(weekFlag);
                double employeeDayWorkTime= employeeDto.getDayWorkTime().get(weekFlag);
                double minEmployeeWeekWorkTime=scheduledEmployeeDto.getWeekWorkTime();
                double employeeWeekWorkTime=employeeDto.getWeekWorkTime();
                if(minEmployeeWeekWorkTime>employeeWeekWorkTime){
                    scheduledEmployeeDto = employeeDto;
                }else if(minEmployeeWeekWorkTime==employeeWeekWorkTime){
                    if(minEmployeeDayWorkTime>employeeDayWorkTime){
                        scheduledEmployeeDto = employeeDto;
                    }else if(minEmployeeDayWorkTime==employeeDayWorkTime){
                        //比较是否有特殊情况

                    }
                }
            }
            scheduleWorkFormDto=scheduleShiftSortDto.getWorkFormDtoList().get(0);
            //安排班次
            scheduleShift(sameShiftSortDtoList.get(0),scheduledEmployeeDto);
        }
        returnShiftSortDto.getList().add(scheduledEmployeeDto);
        returnShiftSortDto.getWorkFormDtoList().add(scheduleWorkFormDto);
        return returnShiftSortDto;
    }
    //将员工排进去
    private static void scheduleShift(ShiftSortDto shiftSortDto,EmployeeDto employeeDto) {
        WorkFormDto workFormDto = shiftSortDto.getWorkFormDtoList().get(0);
        //用于判断星期几
        String week= WeekUtil.dealDateToStringWeek(workFormDto.getDate());
        int weekFlag=WeekUtil.Week.indexOf(week);
        //计算员工的日工作时间和周工作时间
        double dayTime= employeeDto.getDayWorkTime().get(weekFlag);
        employeeDto.getDayWorkTime().set(weekFlag,dayTime + workFormDto.getShiftTime());
        employeeDto.setWeekWorkTime(employeeDto.getWeekWorkTime()+ workFormDto.getShiftTime());
        //将员工排进去
        workFormDto.setEmployeeId(employeeDto.getId());
        //将排完的班次删除,和员工
        shiftSortDto.getWorkFormDtoList().remove(workFormDto);
        shiftSortDto.getList().remove(employeeDto);
    }


    //处理班次，将不符合的员工删除掉
    private static void dealShift(List<ShiftSortDto> shiftSortList,ShiftSortDto scheduleShiftSortDto,Map<String,String> ruleMap) {
        //获取规则值
        JSONObject workTimeJSON=JSON.parseObject(ruleMap.get("工作时长规则"));
        JSONObject lunchTimeJSON=JSON.parseObject(ruleMap.get("午餐时间规则"));
        JSONObject dinnerTimeJSON=JSON.parseObject(ruleMap.get("晚餐时间规则"));
        JSONObject breakTimeJSON=JSON.parseObject(ruleMap.get("休息时间规则"));
        //取得需要用的规则的值
        double maxWorkTime=workTimeJSON.getDouble("maxWorkTime");
        String[] lunchTime=lunchTimeJSON.getString("time").split("-");
        String lunchStartTime=lunchTime[0];
        String lunchEndTime=lunchTime[1];
        double lunchTimeLimit=lunchTimeJSON.getDouble("count");
        String[] dinnerTime=dinnerTimeJSON.getString("time").split("-");
        String dinnerStartTime=dinnerTime[0];
        String dinnerEndTime=dinnerTime[1];
        double dinnerTimeLimit=dinnerTimeJSON.getDouble("count");
        double breakTimeLimit=breakTimeJSON.getDouble("count");

        EmployeeDto scheduledEmployeeDto=scheduleShiftSortDto.getList().get(0);//被排的员工
        WorkFormDto scheduledWorkFormDto=scheduleShiftSortDto.getWorkFormDtoList().get(0);//被排的班次
        String scheduleWorkFormStratTime=scheduledWorkFormDto.getStartTime();
        String scheduleWorkFormEndTime=scheduledWorkFormDto.getEndTime();
        boolean flag=false;//用于判断班次是否被删除
        for(ShiftSortDto shiftSortDto :shiftSortList){
            flag=false;
            //判断班次是否包含了该员工
            if(shiftSortDto.getList().contains(scheduledEmployeeDto)){
                WorkFormDto workFormDto = shiftSortDto.getWorkFormDtoList().get(0);
                //用于判断星期几
                String week= workFormDto.getWeek();
                int weekFlag=WeekUtil.Week.indexOf(week);
                //判断包含了这个员工的班次日工作时间和周工作时间是否加上会超过
                double dayWorkTime= scheduledEmployeeDto.getDayWorkTime().get(weekFlag);
                double weekWorkTime= scheduledEmployeeDto.getWeekWorkTime();
                double dayWorkTimeLimit= scheduledEmployeeDto.getDayWorkTimeLimit();
                double weekWorkTimeLimit= scheduledEmployeeDto.getWeekWorkTimeLimit();
                if(dayWorkTime+ workFormDto.getShiftTime()>dayWorkTimeLimit||
                        weekWorkTime+ workFormDto.getShiftTime()>weekWorkTimeLimit){
                    flag=true;
                }
                if(!flag){
                    //判断是否在同一天
                    if(workFormDto.getWeek().equals(scheduledWorkFormDto.getWeek())){
                        //且两班次之间没有超过休息时间
                        int startAndEndFlag=TimeUtil.compareToStringTime(scheduleWorkFormStratTime,workFormDto.getEndTime());
                        int endAndStartFlag=TimeUtil.compareToStringTime(scheduleWorkFormEndTime,workFormDto.getStartTime());
                        //判断班次在前面
                        if(startAndEndFlag==1){
                            int frontFlag=TimeUtil.compareToStringTime(TimeUtil.CalculateTime(scheduleWorkFormStratTime,-breakTimeLimit),workFormDto.getEndTime());
                            if(frontFlag==-1){
                                flag=true;
                            }
                            //前面相连接
                        }else if(startAndEndFlag==0){
                            //判断是否超过最大连续
                            if(workFormDto.getShiftTime()+scheduledWorkFormDto.getShiftTime()>maxWorkTime){
                                flag=true;
                            }
                        }
                        //判断班次在后面
                        if(endAndStartFlag==-1){
                            int afterFlag=TimeUtil.compareToStringTime(TimeUtil.CalculateTime(scheduleWorkFormEndTime,breakTimeLimit),workFormDto.getStartTime());
                            if(afterFlag==1){
                                flag=true;
                            }
                        }else if(endAndStartFlag==0){
                            //判断是否超过最大连续
                            if(workFormDto.getShiftTime()+scheduledWorkFormDto.getShiftTime()>maxWorkTime){
                                flag=true;
                            }
                        }
                        int endAndEndFlag=TimeUtil.compareToStringTime(scheduleWorkFormEndTime,workFormDto.getEndTime());
                        int startAndStartFlag=TimeUtil.compareToStringTime(scheduleWorkFormStratTime,workFormDto.getStartTime());
                        //判断是否有重复段
                        if(startAndEndFlag==-1){
                            if(endAndEndFlag==1||endAndEndFlag==0){
                                flag=true;

                            }
                        }
                        if(endAndStartFlag==1){
                            if(endAndEndFlag==-1){
                                flag=true;
                            }
                        }
                        //被包含
                        if(endAndEndFlag==-1&&startAndStartFlag==1){
                            flag=true;
                        }

                    }
                }

            }
            if(flag){
                shiftSortDto.getList().remove(scheduledEmployeeDto);
            }
        }
        //判断午餐和晚餐时间
        //判断是否有某个班次没有人，将其删除
        shiftSortList.removeIf(shiftSortDto1 -> shiftSortDto1.getWorkFormDtoList().size() == 0||shiftSortDto1.getList().size()==0);
    }


    //生成客流量班次
    public static void getShift(List<List<WorkFormDto>> sortLists, ShiftDto shiftDto, double maxShiftTime, double plusShiftTime){
        int shiftPeople= shiftDto.getPeopleNum();
        //获取与当前班次的开始时间一样的前面的班次集合
        List<List<WorkFormDto>> lastWorkFormLists=new ArrayList<>();
        sortLists.forEach(l->{
            l.forEach(r->{
                log.info(r.toString());
            });
        });
        for(List<WorkFormDto> list:sortLists){
            if(TimeUtil.compareToStringTime(list.get(0).getEndTime(), shiftDto.getStartTime())==0&&list.get(0).getShiftTime()<maxShiftTime){
                lastWorkFormLists.add(list);
            }
        }
        //对要延长的班次按班次时间升序
        List<List<WorkFormDto>> sortupList=new ArrayList<>();
        int length=lastWorkFormLists.size();
        for(int a=0;a<length;a++){
            int minIndex=0;
            for(int b=0;b<lastWorkFormLists.size();b++){
                if(lastWorkFormLists.get(minIndex).get(0).getShiftTime()>lastWorkFormLists.get(b).get(0).getShiftTime()){
                    minIndex=b;
                }
            }
            sortupList.add(lastWorkFormLists.get(minIndex));
            lastWorkFormLists.remove(minIndex);
        }
        if(sortupList.size()!=0){
            lastWorkFormLists=sortupList;
        }

        //循环处理每一个是否可以延申
        for(List<WorkFormDto> lastWorkFormDtoList :lastWorkFormLists){
            WorkFormDto lastWorkFormDto = lastWorkFormDtoList.get(0);//注意使用时可能值会被改变，因为后面对其进行修改，这里赋值的时地址
            if(lastWorkFormDto.getShiftTime()<maxShiftTime) {
                //分两种情况： 刚好满足加一个小时   不满足
                if(lastWorkFormDto.getShiftTime()+plusShiftTime>maxShiftTime){
                    //不满足
                    //分三种情况：1.等于直接修改，然后添加新的 2.小于需要修改原来的，并且需要添加新的   3.大于需要取出来生成新的集合，并且添加
                    if (lastWorkFormDtoList.size() < shiftPeople) {
                        //小于
                        //用于保存延长时间前的，需要加的班次时间
                        double shiftTime=maxShiftTime- lastWorkFormDto.getShiftTime();//还没被改，下方循环才被修改了值
                        shiftPeople -= lastWorkFormDtoList.size();
                        for (WorkFormDto workFormDto : lastWorkFormDtoList) {
                            workFormDto.setEndTime(TimeUtil.CalculateTime(workFormDto.getEndTime(),shiftTime));
                            workFormDto.setShiftTime(workFormDto.getShiftTime() + shiftTime);
                        }
                        //补齐
                        WorkFormDto workFormDto =new WorkFormDto();
                        workFormDto.setDate(shiftDto.getDate());
                        workFormDto.setAllowCareer("all");
                        workFormDto.setStartTime(lastWorkFormDto.getEndTime());
                        workFormDto.setEndTime(shiftDto.getEndTime());
                        workFormDto.setShiftTime(shiftTime);
                        sortLists.add(BeanCopyUtil.copyWorkFormBean(lastWorkFormDtoList.size(), workFormDto));
                    } else if (lastWorkFormDtoList.size() == shiftPeople) {
                        //等于
                        //用于保存延长时间前的，需要加的班次时间
                        double shiftTime=maxShiftTime- lastWorkFormDto.getShiftTime();
                        shiftPeople -= lastWorkFormDtoList.size();
                        for (WorkFormDto workFormDto : lastWorkFormDtoList) {
                            workFormDto.setEndTime(TimeUtil.CalculateTime(workFormDto.getEndTime(),shiftTime));
                            workFormDto.setShiftTime(workFormDto.getShiftTime() +shiftTime);
                        }
                        //补齐
                        WorkFormDto workFormDto =new WorkFormDto();
                        workFormDto.setDate(shiftDto.getDate());
                        workFormDto.setAllowCareer("all");
                        workFormDto.setStartTime(lastWorkFormDto.getEndTime());
                        workFormDto.setEndTime(shiftDto.getEndTime());
                        workFormDto.setShiftTime(shiftTime);
                        sortLists.add(BeanCopyUtil.copyWorkFormBean(lastWorkFormDtoList.size(), workFormDto));
                    } else {
                        //大于
                        //用于保存延长时间前的，需要加的班次时间
                        double shiftTime=maxShiftTime- lastWorkFormDto.getShiftTime();
                        List<WorkFormDto> list = new ArrayList<>();
                        for (int a = 0; a < shiftPeople; a++) {
                            WorkFormDto remove = lastWorkFormDtoList.remove(lastWorkFormDtoList.size() - 1);
                            remove.setEndTime(TimeUtil.CalculateTime(remove.getEndTime(),shiftTime));
                            remove.setShiftTime(remove.getShiftTime() + shiftTime);
                            list.add(remove);
                        }
                        sortLists.add(list);
                        shiftPeople = 0;

                        //补齐
                        WorkFormDto workFormDto =new WorkFormDto();
                        workFormDto.setDate(shiftDto.getDate());
                        workFormDto.setAllowCareer("all");
                        workFormDto.setStartTime(list.get(0).getEndTime());
                        workFormDto.setEndTime(shiftDto.getEndTime());
                        workFormDto.setShiftTime(shiftTime);
                        sortLists.add(BeanCopyUtil.copyWorkFormBean(list.size(), workFormDto));
                    }

                }else{
                    //满足
                    //分三种情况：1.等于直接修改 2.小于需要，需要添加   3.大于需要取出来生成新的集合
                    if (lastWorkFormDtoList.size() < shiftPeople) {
                        //小于   后面添加
                        shiftPeople -= lastWorkFormDtoList.size();
                        for (WorkFormDto workFormDto : lastWorkFormDtoList) {
                            workFormDto.setEndTime(shiftDto.getEndTime());
                            workFormDto.setShiftTime(workFormDto.getShiftTime() + plusShiftTime);
                        }
                    } else if (lastWorkFormDtoList.size() == shiftPeople) {
                        //等于
                        shiftPeople -= lastWorkFormDtoList.size();
                        for (WorkFormDto workFormDto : lastWorkFormDtoList) {
                            workFormDto.setEndTime(shiftDto.getEndTime());
                            workFormDto.setShiftTime(workFormDto.getShiftTime() + plusShiftTime);
                        }
                    } else {
                        //大于
                        List<WorkFormDto> createWorkFormlistDto = new ArrayList<>();
                        for (int a = 0; a < shiftPeople; a++) {
                            WorkFormDto remove = lastWorkFormDtoList.remove(lastWorkFormDtoList.size() - 1);
                            remove.setEndTime(shiftDto.getEndTime());
                            remove.setShiftTime(remove.getShiftTime() + plusShiftTime);
                            createWorkFormlistDto.add(remove);
                        }
                        sortLists.add(createWorkFormlistDto);
                        shiftPeople = 0;
                    }
                }
            }
            if(shiftPeople==0){
                break;
            }
        }
        if(shiftPeople!=0){
            //生成剩下的班次
            /*WorkForm workForm=new WorkForm();
            workForm.setDate(LocalDate.now().toString());
            workForm.setAllowCareer("all");
            workForm.setStartTime(shiftVo.getStartTime());
            workForm.setEndTime(shiftVo.getEndTime());
            workForm.setShiftTime(1.0);
            sortLists.add(BeanCopyUtil.copyWorkFormBean(shiftPeople,workForm));*/

            //判断班次的时间是否超过了最大时间限制
            if(plusShiftTime>maxShiftTime){
                //超过了最大时间限制，分成两个类型班次生成
                WorkFormDto Shift1=new WorkFormDto();//前面的班次
                WorkFormDto Shift2=new WorkFormDto();//后面的班次
                Shift1.setDate(shiftDto.getDate());
                Shift1.setStartTime(shiftDto.getStartTime());
                Shift1.setEndTime(TimeUtil.CalculateTime(shiftDto.getStartTime(),4.0));
                Shift1.setAllowCareer("all");
                Shift1.setShiftTime(4.0);

                Shift2.setStartTime(Shift1.getEndTime());
                Shift2.setEndTime(shiftDto.getEndTime());
                Shift2.setDate(shiftDto.getDate());
                Shift2.setAllowCareer("all");
                Shift2.setShiftTime(plusShiftTime-maxShiftTime);
                //通过开店班次人数生成多个相同班次
                sortLists.add(BeanCopyUtil.copyWorkFormBean(shiftPeople,Shift1));
                sortLists.add(BeanCopyUtil.copyWorkFormBean(shiftPeople,Shift2));
            }else{
                //没超过时间限制
                WorkFormDto Shift=new WorkFormDto();
                Shift.setStartTime(shiftDto.getStartTime());
                Shift.setEndTime(shiftDto.getEndTime());
                Shift.setAllowCareer("all");
                Shift.setDate(shiftDto.getDate());
                Shift.setShiftTime(plusShiftTime);
                //通过开店班次人数生成多个相同班次
                sortLists.add(BeanCopyUtil.copyWorkFormBean(shiftPeople,Shift));
            }
        }
    }

    //统计每一个班次的满足人数
    public static void totalShiftPeople(List<ShiftSortDto> shiftSortDtoList, List<EmployeeDto> employeeDtoList, List<List<List<WorkFormDto>>> shiftLists) {
        for(List<List<WorkFormDto>> workFormDtoLists :shiftLists){
            for(List<WorkFormDto> workFormDto : workFormDtoLists){
                ShiftSortDto shiftSortDto =new ShiftSortDto();
                List<WorkFormDto> workFormDtos = new ArrayList<>(workFormDto);
                shiftSortDto.setWorkFormDtoList(workFormDtos);
                List<EmployeeDto> list= shiftSortDto.getList();
                for(EmployeeDto employeeDto : employeeDtoList){
                    //判断是否符合日工作时间
                    if(employeeDto.getDayWorkTimeLimit()!=0&& employeeDto.getDayWorkTimeLimit()< workFormDto.get(0).getShiftTime()){
                        break;
                    }
                    //判断是否符合职位要求
                    if(workFormDto.get(0).getAllowCareer().equals("all")|| workFormDto.get(0).getAllowCareer().contains(employeeDto.getPosition())){
                        //判断是否有规则限制
                        if(employeeDto.getPreferenceVoMap().size()==0){
                            list.add(employeeDto);
                        }else{
                            //判断是否符合时间要求
                            //获取某星期的限制
                            EmployeePreferenceDto preferenceVo= employeeDto.getPreferenceVoMap().get(workFormDto.get(0).getWeek());
                            if (preferenceVo!=null){
                                int startFlag=TimeUtil.compareToStringTime(preferenceVo.getStartTime(), workFormDto.get(0).getStartTime());
                                int endFlag=TimeUtil.compareToStringTime(preferenceVo.getEndTime(), workFormDto.get(0).getEndTime());
                                if((startFlag==0||startFlag==-1)&&(endFlag==0||endFlag==1)){
                                    list.add(employeeDto);
                                }
                            }
                        }
                    }

                }
                shiftSortDtoList.add(shiftSortDto);
            }
        }
        //将没有人的班次移除要排班的集合
        shiftSortDtoList.removeIf(shiftSortDto -> shiftSortDto.getList().size() == 0);
    }

}
