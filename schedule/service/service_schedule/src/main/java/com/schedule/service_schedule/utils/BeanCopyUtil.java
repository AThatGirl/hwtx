package com.schedule.service_schedule.utils;

import com.schedule.service_schedule.entity.dto.WorkFormDto;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class BeanCopyUtil {
    //复制多个类
    public static List<WorkFormDto> copyWorkFormBean(int num, WorkFormDto bean){
        List<WorkFormDto> list=new ArrayList<>();
        for(int i=0;i<num;i++){
            WorkFormDto workFormDto =new WorkFormDto();
            BeanUtils.copyProperties(bean, workFormDto);
            list.add(workFormDto);
        }
        return list;
    }
}
