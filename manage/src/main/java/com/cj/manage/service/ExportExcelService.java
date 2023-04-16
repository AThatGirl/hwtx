package com.cj.manage.service;


import javax.servlet.http.HttpServletResponse;

/**
 * 导出到excel
 */
public interface ExportExcelService {

    /**
     * 导出所有用户
     */

    void writeUserToExcel(String storeId, HttpServletResponse response);

    /**
     * 导出请假条
     */
    void writeWrittenToExcel(String storeId, HttpServletResponse response);
    /**
     * 导出消息
     */
    void writeNoticeToExcel(String storeId,HttpServletResponse response);
    /**
     * 导出建议
     */
    void writeSuggestToExcel(String storeId,HttpServletResponse response);

}
