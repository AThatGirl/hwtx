package com.cj.manage.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import lombok.Data;
import org.apache.poi.ss.usermodel.FillPatternType;

@Data
@HeadStyle(fillPatternType = FillPatternType.NO_FILL, fillForegroundColor = 10)
public class NoticeExcel {

    @ExcelProperty(value = "发送人", index = 0)
    private String senderName;
    @ExcelProperty(value = "接收人", index = 1)
    private String receiverName;
    @ExcelProperty(value = "消息内容", index = 2)
    private String content;
    @ExcelProperty(value = "发送时间", index = 3)
    private String createTime;
    @ExcelProperty(value = "门店", index = 4)
    private String storeName;
}
