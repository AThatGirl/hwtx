package com.cj.manage.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import lombok.Data;
import org.apache.poi.ss.usermodel.FillPatternType;

@Data
@HeadStyle(fillPatternType = FillPatternType.NO_FILL, fillForegroundColor = 10)
public class SuggestExcel {
    @ExcelProperty(value = "姓名", index = 0)
    private String content;
    @ExcelProperty(value = "提交时间", index = 1)
    private String subTime;
    @ExcelProperty(value = "姓名", index = 2)
    private String storeName;
}
