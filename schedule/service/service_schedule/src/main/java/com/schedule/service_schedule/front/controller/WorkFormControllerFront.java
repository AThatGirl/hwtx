package com.schedule.service_schedule.front.controller;


import com.schedule.service_schedule.front.service.RuleServiceFront;
import com.schedule.service_schedule.front.service.WorkFormServiceFront;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  班次接口
 * </p>
 *
 * @author tpt
 * @since 2023-02-17
 */
@RestController
@RequestMapping("/com/schedule/service_schedule/front/work-form")
@Api(tags = "排班接口")
public class WorkFormControllerFront {
    @Resource
    private WorkFormServiceFront workFormServiceFront;

    @Resource
    private RuleServiceFront ruleServiceFront;


}

