package com.cj.manage.feign;


import com.cj.common.vo.ResultVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "service-schedule", url = "http://localhost:8021/service_schedule/admin/rule")
public interface RuleFeign {

    @PostMapping("/insert-store-rule/{id}")
    @ApiOperation(value = "插入门店规则")
    ResultVO insertScheduleRule(
            @ApiParam(name = "id",value = "门店id",required = true) @PathVariable String id);

}
