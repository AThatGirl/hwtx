package com.schedule.service_schedule.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.schedule.service_base.handler.exceptionHandler.ScheduleException;
import com.schedule.service_schedule.entity.Rule;
import com.schedule.service_schedule.entity.dto.RunDateTimeDto;
import com.schedule.service_schedule.entity.vo.rule.RuleVo;
import com.schedule.service_schedule.mapper.RuleMapper;
import com.schedule.service_schedule.admin.service.RuleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.schedule.service_schedule.utils.RuleUtil;
import com.schedule.service_schedule.utils.WeekUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tpt
 * @since 2023-02-12
 */
@Service
public class RuleServiceImpl extends ServiceImpl<RuleMapper, Rule> implements RuleService {
    //插入默认门店规则
    @Override
    @Transactional
    public void insertRule(String id) {
        //循环插入默认规则
        try{
            for (Rule rule : RuleUtil.ruleList) {
                rule.setStoreId(id);
                baseMapper.insert(rule);
            }
        }catch (Exception e){
            throw new ScheduleException(20001,"插入默认门店规则出错:"+e.getMessage());
        }

    }

    //更新门店规则
    @Override
    public void update(RuleVo ruleVo) {
        //通过门店id和规则类型更新数据
        QueryWrapper<Rule> wrapper=new QueryWrapper<>();
        Rule rule=new Rule();
        rule.setStoreId(ruleVo.getStoreId());
        RuleUtil.setRuleByRuleVo(ruleVo,rule);
        wrapper.eq("type",rule.getType());
        wrapper.eq("store_id",rule.getStoreId());
        baseMapper.update(rule,wrapper);

    }

    //获取规则
    @Override
    public RuleVo getStoreRules(String storeId) {
        RuleVo ruleVo=new RuleVo();
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("store_id",storeId);
        List<Rule> ruleList=baseMapper.selectList(wrapper);
        RuleUtil.setRuleVoByRule(ruleVo,ruleList);
        return ruleVo;
    }
}
