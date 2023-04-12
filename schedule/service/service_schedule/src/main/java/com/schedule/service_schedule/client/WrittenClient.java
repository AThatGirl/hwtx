package com.schedule.service_schedule.client;

import com.schedule.service_schedule.client.impl.EmployeeClientImpl;
import com.schedule.service_schedule.client.impl.WrittenClientImpl;
import com.schedule.service_schedule.entity.vo.client.Written;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@Component
@FeignClient(name = "written" ,fallback = WrittenClientImpl.class)
public interface WrittenClient {
    @GetMapping("/written/written/getWrittenByEmployeeId/{employeeId}")
    @ApiOperation("通过用户id获取请假条")
    Written getWrittenByEmployeeId(@PathVariable String employeeId);
}
