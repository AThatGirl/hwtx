package com.schedule.service_schedule.entity.vo.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ShiftExcelVo implements Serializable {
    @ExcelProperty("日期")
    private String date;
    @ExcelProperty("开始时间")
    private String startTime;
    @ExcelProperty("结束时间")
    private String endTime;
    @ExcelProperty("员工姓名")
    private String employeeName;
    @ExcelProperty("联系电话")
    private String phone;
}
