package com.schedule.service_schedule.client;

import com.schedule.service_schedule.client.impl.EmployeeClientImpl;
import com.schedule.service_schedule.entity.vo.client.Preference;
import com.schedule.service_schedule.entity.vo.client.User;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Component
@FeignClient(name = "personal" ,fallback = EmployeeClientImpl.class)
public interface EmployeeClient {
    @GetMapping("/personal/message/getAllEmployeeMessage/{storeId}")
    List<User> getAllEmployeeMessage(@PathVariable String storeId);

    @GetMapping("/personal/preference/getPreference/{employeeid}")
    @ApiOperation("获取偏好信息")
     Preference getPreference(@PathVariable("employeeid") String id);


}
