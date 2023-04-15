package com.schedule.service_schedule.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.schedule.service_base.handler.exceptionHandler.ScheduleException;
import com.schedule.service_schedule.entity.dto.PassengerFlowExcelDto;
import com.schedule.service_schedule.admin.service.RuleService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class PassengerFlowExcelListener extends AnalysisEventListener<PassengerFlowExcelDto> {
    private List<PassengerFlowExcelDto> passengerFlowExcelDtoList =null;//存放客流量数据
    private List<List<PassengerFlowExcelDto>> passengerFlowExcelVoLists=new ArrayList<>();//存放多张表的客流量数据

    private RuleService ruleService;

    private String storeId;
    public PassengerFlowExcelListener(){

    }
    //一行一行去读取excle内容
    @Override
    public void invoke(PassengerFlowExcelDto passengerFlowExcelDto, AnalysisContext analysisContext) {
        if(passengerFlowExcelDto ==null){
            throw new ScheduleException(20001,"读取客流量excel文件失败！");
        }
        if(passengerFlowExcelDtoList ==null||!passengerFlowExcelDtoList.get(0).getDate().equals(passengerFlowExcelDto.getDate())){
            passengerFlowExcelDtoList =new ArrayList<>();
        }

        passengerFlowExcelDtoList.add(passengerFlowExcelDto);
    }

    //读取excel表头信息
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头信息："+headMap);
    }

    //读取完成后执行
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        //passengerFlowExcelVoList.forEach(System.out::println);
        passengerFlowExcelVoLists.add(passengerFlowExcelDtoList);
    }

    public List<List<PassengerFlowExcelDto>> getPassengerFlowExcelVoLists() {
        return passengerFlowExcelVoLists;
    }

}
