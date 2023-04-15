package com.schedule.service_schedule.admin.service;

import com.schedule.service_schedule.entity.WorkForm;
import com.baomidou.mybatisplus.extension.service.IService;
import com.schedule.service_schedule.entity.dto.PassengerFlowExcelDto;
import com.schedule.service_schedule.entity.vo.client.User;
import com.schedule.service_schedule.entity.vo.excel.ShiftExcelVo;
import com.schedule.service_schedule.entity.vo.workForm.ShiftVo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tpt
 * @since 2023-02-17
 */
public interface WorkFormService extends IService<WorkForm> {
    //插入班次
    void insertWorkForm(List<List<PassengerFlowExcelDto>> passengerFlowExcelVoLists, Map<String,String> ruleMap, double storeArea, String storeId);
    //获取门店某个时间的班次
    List<List<List<WorkForm>>> getWorkFormByStoreId(String storeId, LocalDate startDate,LocalDate endDate);

    List<WorkForm> getDayShift(String storeId, LocalDate date,String position,String employeeName);

    List<List<WorkForm>> getWeekShift(String storeId, LocalDate startDate, LocalDate endDate,String position,String employeeName);

    List<List<List<WorkForm>>> scheduleShifts(String storeId, LocalDate startDate, LocalDate endDate);

    void updateById(List<List<List<WorkForm>>> shifts);

    List<User> getConformEmployee(ShiftVo shiftVo);

    List<List<ShiftExcelVo>> getShiftExcelInfo(String storeId, LocalDate startDate,LocalDate endDate);

    List<List<WorkForm>> getMonthShifts(String storeId, LocalDate startDate, LocalDate endDate);

    Object getEmployeeWorkHour(String employeeId, LocalDate date, LocalDate startDate, LocalDate endDate);
}
