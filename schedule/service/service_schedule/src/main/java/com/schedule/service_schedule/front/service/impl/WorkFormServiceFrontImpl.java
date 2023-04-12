package com.schedule.service_schedule.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.schedule.service_schedule.front.service.WorkFormServiceFront;
import com.schedule.service_schedule.entity.WorkForm;
import com.schedule.service_schedule.mapper.WorkFormMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tpt
 * @since 2023-02-17
 */
@Slf4j
@Service
public class WorkFormServiceFrontImpl extends ServiceImpl<WorkFormMapper, WorkForm> implements WorkFormServiceFront {

    /**
     * 获取班次通过门店id和时间范围
     * @param storeId
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public List<List<List<WorkForm>>> getWorkFormByStoreId(String storeId, LocalDate startDate, LocalDate endDate) {
        List<List<List<WorkForm>>> lists=new ArrayList<>();
        List<List<WorkForm>> listList=new ArrayList<>();
        List<WorkForm> workFormList=new ArrayList<>();
        LocalDate flagDate=null;
        LocalTime flagStartTime=null;
        LocalTime flagEndTime=null;
        QueryWrapper<WorkForm> wrapper=new QueryWrapper<>();
        wrapper.between("date",startDate,endDate);
        wrapper.orderByAsc("date","start_time","end_time");
        List<WorkForm> list=baseMapper.selectList(wrapper);
        log.info(list.size()+"");
        for(WorkForm workForm:list){
            if(flagDate==null){
                flagDate=workForm.getDate();
            }else if(flagDate.compareTo(workForm.getDate())!=0){
                //日期不相同
                flagDate=workForm.getDate();
                listList.add(workFormList);
                lists.add(listList);
                listList=new ArrayList<>();
            }
            if(flagStartTime==null){
                flagStartTime=workForm.getStartTime();
                flagEndTime=workForm.getEndTime();
                workFormList.add(workForm);
            }else if(flagEndTime.compareTo(workForm.getEndTime())==0&&flagStartTime.compareTo(workForm.getStartTime())==0){
                workFormList.add(workForm);
            }else {
                flagEndTime=workForm.getEndTime();
                flagStartTime=workForm.getStartTime();
                listList.add(workFormList);
                workFormList=new ArrayList<>();
                workFormList.add(workForm);
            }
        }
        listList.add(workFormList);
        lists.add(listList);
        return lists;
    }
}
