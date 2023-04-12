package com.schedule.service_schedule.utils;

import com.schedule.service_schedule.entity.WorkForm;
import com.schedule.service_schedule.entity.dto.WorkFormDto;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WorkFormDtoUtil {
    public static List<List<List<WorkFormDto>>> dealWorkFormToWorkFormDto(List<List<List<WorkForm>>> workFormLists){
        List<List<List<WorkFormDto>>> workFormDtoLists=new ArrayList<>();
        for(List<List<WorkForm>> workFormList:workFormLists){
            List<List<WorkFormDto>> workFormDtoList=new ArrayList<>();
            for(List<WorkForm> workForms:workFormList){
                List<WorkFormDto> workFormDtos=new ArrayList<>();
                for(WorkForm workForm:workForms){
                    WorkFormDto workFormDto=new WorkFormDto();
                    BeanUtils.copyProperties(workForm,workFormDto);
                    workFormDto.setStartTime(workForm.getStartTime().toString());
                    workFormDto.setEndTime(workForm.getEndTime().toString());
                    workFormDto.setDate(workForm.getDate().toString());
                    workFormDto.setShiftTime(TimeUtil.calculateStringTimeSub(workFormDto.getStartTime(),workFormDto.getEndTime()));
                    workFormDto.setWeek(WeekUtil.dealDateToStringWeek(workFormDto.getDate()));
                    workFormDtos.add(workFormDto);
                }
                workFormDtoList.add(workFormDtos);
            }
            workFormDtoLists.add(workFormDtoList);
        }
        return workFormDtoLists;
    }

    public static List<List<List<WorkForm>>> dealWorkFormDtoToWorkForm(List<List<List<WorkFormDto>>> workFormDtoLists){
        List<List<List<WorkForm>>> workFormLists=new ArrayList<>();
        for(List<List<WorkFormDto>> workFormDtoList:workFormDtoLists){
            List<List<WorkForm>> workFormList=new ArrayList<>();
            for(List<WorkFormDto> workFormDtos:workFormDtoList){
                List<WorkForm> workForms=new ArrayList<>();
                for(WorkFormDto workFormDto:workFormDtos){
                    WorkForm workForm=new WorkForm();
                    BeanUtils.copyProperties(workFormDto,workForm);
                    workForm.setStartTime(TimeUtil.stringTimeToLocalTime(workFormDto.getStartTime()));
                    workForm.setEndTime(TimeUtil.stringTimeToLocalTime(workFormDto.getEndTime()));
                    workForm.setDate(LocalDate.parse(DateUtil.dealDateFormat(workFormDto.getDate())));
                    workForms.add(workForm);
                }
                workFormList.add(workForms);
            }
            workFormLists.add(workFormList);
        }
        return workFormLists;
    }
}
