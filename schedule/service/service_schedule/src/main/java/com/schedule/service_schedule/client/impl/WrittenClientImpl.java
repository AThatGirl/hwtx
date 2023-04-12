package com.schedule.service_schedule.client.impl;

import com.schedule.service_base.handler.exceptionHandler.ScheduleException;
import com.schedule.service_schedule.client.WrittenClient;
import com.schedule.service_schedule.entity.vo.client.Written;

public class WrittenClientImpl implements WrittenClient {
    @Override
    public Written getWrittenByEmployeeId(String employeeId) {
        throw new ScheduleException(20001,"获取请假条信息超时");
    }
}
