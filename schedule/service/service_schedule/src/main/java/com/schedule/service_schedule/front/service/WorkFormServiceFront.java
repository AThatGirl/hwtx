package com.schedule.service_schedule.front.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.schedule.service_schedule.entity.WorkForm;
import com.schedule.service_schedule.entity.dto.PassengerFlowExcelDto;

import java.time.LocalDate;
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
public interface WorkFormServiceFront extends IService<WorkForm> {
    //获取门店某个时间的班次
    List<List<List<WorkForm>>> getWorkFormByStoreId(String storeId, LocalDate startDate,LocalDate endDate);
}
