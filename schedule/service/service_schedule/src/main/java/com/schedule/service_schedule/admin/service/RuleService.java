package com.schedule.service_schedule.admin.service;

import com.schedule.service_schedule.entity.Rule;
import com.baomidou.mybatisplus.extension.service.IService;
import com.schedule.service_schedule.entity.vo.rule.RuleVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tpt
 * @since 2023-02-12
 */
public interface RuleService extends IService<Rule> {
    //插入默认门店规则
    void insertRule(String id);
    //更新规则
    void update(RuleVo rule);

    RuleVo getStoreRules(String storeId);
}
