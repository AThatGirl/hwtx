package com.cj.care.job;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cj.care.feign.MqNoticeFeign;
import com.cj.care.utils.DataCheckUtils;
import com.cj.care.vo.BlessingVO;
import com.cj.common.en.UserCareer;
import com.cj.common.entity.User;
import com.cj.common.mapper.StoreMapper;
import com.cj.common.mapper.UserMapper;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class BlessingElasticJob implements SimpleJob {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StoreMapper storeMapper;

    @Autowired
    private MqNoticeFeign mqNoticeFeign;


    @Override
    public void execute(ShardingContext shardingContext) {
        //查询到所有员工的生日
        List<User> users = userMapper.selectList(new QueryWrapper<User>().ne("career", UserCareer.ADMIN.getCareer()).ne("career", UserCareer.SUPER_ADMIN.getCareer()).eq("pass", User.YES_PASS));
        Date currentDate = new Date();
        for (User user : users) {
            //查询该门店下的管理员，由该门店的管理员发送邮箱
            User admin = userMapper.selectOne(new QueryWrapper<User>().eq("store_id", user.getStoreId()).eq("career", UserCareer.ADMIN.getCareer()));
            log.info("{}", admin);
            //判断是否今天生日
            if (DataCheckUtils.isBirthday(user.getBirthday())) {
                //设置消息并发送
                BlessingVO blessingVO = new BlessingVO();
                blessingVO.setSenderEmail(admin.getEmail());
                blessingVO.setReceiverEmail(user.getEmail());
                blessingVO.setContent("亲爱的" + user.getName() + ",祝您生日快乐!");
                mqNoticeFeign.sendBlessing(JSONObject.toJSONString(blessingVO));
                log.info("发送生日祝福到消息队列成功");
            }
            if (!DataCheckUtils.NOT_HOLIDAY.equals(DataCheckUtils.isHoliday(currentDate))) {
                //设置消息并发送
                BlessingVO blessingVO = new BlessingVO();
                blessingVO.setSenderEmail(admin.getEmail());
                blessingVO.setReceiverEmail(user.getEmail());
                blessingVO.setContent("亲爱的" + user.getName() + ",祝您" + DataCheckUtils.isHoliday(currentDate) + "快乐!");
                mqNoticeFeign.sendBlessing(JSONObject.toJSONString(blessingVO));
                log.info("发送节日祝福到消息队列成功");
            }
        }
    }
}