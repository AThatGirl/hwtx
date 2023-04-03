package com.cj.manage.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import lombok.Data;
import org.apache.poi.ss.usermodel.FillPatternType;

@Data
@HeadStyle(fillPatternType = FillPatternType.NO_FILL, fillForegroundColor = 10)
public class WrittenExcel {
    @ExcelProperty(value = "请假原因", index = 0)
    private String reason;
    @ExcelProperty(value = "请假开始时间", index = 1)
    private String startTime;
    @ExcelProperty(value = "请假结束时间", index = 2)
    private String endTime;
    @ExcelProperty(value = "请假状态", index = 3)
    private String status;
    @ExcelProperty(value = "请假发起时间", index = 4)
    private String createTime;
    @ExcelProperty(value = "请假修改时间", index = 5)
    private String updateTime;
    @ExcelProperty(value = "请假员工", index = 6)
    private String employeeName;
    @ExcelProperty(value = "所属门店", index = 7)
    private String storeName;
}
