package com.cj.manage.controller;

import com.cj.manage.service.ExportExcelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/manage/exportExcel")
@Api(tags = "导出excel", value = "导出excel")
public class ExportExcelController {

    @Autowired
    private ExportExcelService exportExcelService;

    @ApiOperation("导出用户信息")
    @GetMapping("/writeUserToExcel/{storeId}")
    public void writeUserToExcel(@PathVariable String storeId, HttpServletResponse response) {
        exportExcelService.writeUserToExcel(storeId, response);
    }

    @ApiOperation("导出请假信息")
    @GetMapping("/writeWrittenToExcel/{storeId}")
    public void writeWrittenToExcel(@PathVariable String storeId, HttpServletResponse response){
        exportExcelService.writeWrittenToExcel(storeId, response);
    }
    @ApiOperation("导出通知信息")
    @GetMapping("/writeNoticeToExcel/{storeId}")
    public void writeNoticeToExcel(@PathVariable String storeId,HttpServletResponse response){
        exportExcelService.writeNoticeToExcel(storeId, response);
    }
    @ApiOperation("导出建议信息")
    @GetMapping("/writeSuggestToExcel/{storeId}")
    public void writeSuggestToExcel(@PathVariable String storeId,HttpServletResponse response){
        exportExcelService.writeSuggestToExcel(storeId, response);
    }

}
