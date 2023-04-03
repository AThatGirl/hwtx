package com.cj.manage.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import lombok.Data;
import org.apache.poi.ss.usermodel.FillPatternType;

@Data
@HeadStyle(fillPatternType = FillPatternType.NO_FILL, fillForegroundColor = 10)
public class UserInfoExcel {

    @ExcelProperty(value = "姓名", index = 0)
    private String name;
    @ExcelProperty(value = "年龄", index = 1)
    private Integer age;
    @ExcelProperty(value = "性别", index = 2)
    private String sex;
    @ExcelProperty(value = "出生日期", index = 3)
    private String birthday;
    @ExcelProperty(value = "手机号", index = 4)
    private String phone;
    @ExcelProperty(value = "职业", index = 5)
    private String career;
    @ExcelProperty(value = "邮箱", index = 6)
    private String email;
    @ExcelProperty(value = "门店名称", index = 7)
    private String storeName;

}
