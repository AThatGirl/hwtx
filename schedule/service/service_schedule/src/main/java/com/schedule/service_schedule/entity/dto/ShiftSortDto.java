package com.schedule.service_schedule.entity.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ShiftSortDto implements Comparable<ShiftSortDto>{
    private List<WorkFormDto> workFormDtoList=new ArrayList<>();
    private List<EmployeeDto> list=new ArrayList<>();

    @Override
    public int compareTo(ShiftSortDto shiftSortDto) {
        return this.list.size()- shiftSortDto.getList().size();
    }
}
