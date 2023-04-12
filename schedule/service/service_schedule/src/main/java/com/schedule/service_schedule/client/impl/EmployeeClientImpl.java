package com.schedule.service_schedule.client.impl;

import com.schedule.service_base.handler.exceptionHandler.ScheduleException;
import com.schedule.service_schedule.client.EmployeeClient;
import com.schedule.service_schedule.entity.vo.client.Preference;
import com.schedule.service_schedule.entity.vo.client.User;

import java.util.List;

public class EmployeeClientImpl implements EmployeeClient {
    @Override
    public List<User> getAllEmployeeMessage(String storeId) {
        throw new ScheduleException(20001,"获取员工数据超时");
    }

    @Override
    public Preference getPreference(String id) {
        throw new ScheduleException(20001,"获取员工偏好超时");
    }
}
