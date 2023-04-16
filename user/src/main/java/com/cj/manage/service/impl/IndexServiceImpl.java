package com.cj.manage.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cj.common.entity.ClockIn;
import com.cj.common.entity.User;
import com.cj.common.entity.Written;
import com.cj.common.mapper.ClockInMapper;
import com.cj.common.mapper.UserMapper;
import com.cj.common.mapper.WrittenMapper;
import com.cj.common.vo.ResultVO;
import com.cj.manage.service.IndexService;
import com.cj.manage.utils.DateCheckUtils;
import com.cj.manage.vo.ClockInWeekVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WrittenMapper writtenMapper;

    @Autowired
    private ClockInMapper clockInMapper;

    @Override
    public ResultVO indexNum() {
        int userNum = userMapper.selectList(null).size();
        int userPassingNum = userMapper.selectList(new QueryWrapper<User>().eq("pass", User.PASSING)).size();
        int writtenPassingNum = writtenMapper.selectList(new QueryWrapper<Written>().eq("status", Written.AUDIT_STATUS)).size();
        Map<String, Integer> map = new HashMap<>();
        map.put("userNum", userNum);
        map.put("userPassingNum", userPassingNum);
        map.put("writtenPassingNum", writtenPassingNum);
        return ResultVO.success().setData(map);
    }

    @Override
    public ResultVO getClockInNumWeek(String storeId) {

        List<ClockIn> clockIns = clockInMapper.selectList(new QueryWrapper<ClockIn>().eq("store_id", storeId));
        int[] clockInUp = new int[7];
        int[] clockInDown = new int[7];
        for (ClockIn clockIn : clockIns) {
            if (DateCheckUtils.isWithinWeek(clockIn.getSignTime())) {
                if (clockIn.getSignType().equals(ClockIn.SIGN_UP)) {
                    int up = DateCheckUtils.getDaysBetween(DateCheckUtils.getTodayDate(), clockIn.getSignTime());
                    clockInUp[up] += 1;
                }
                if (clockIn.getSignType().equals(ClockIn.SIGN_DOWN)) {
                    int down = DateCheckUtils.getDaysBetween(DateCheckUtils.getTodayDate(), clockIn.getSignTime());
                    clockInDown[down] += 1;
                }
            }
        }
        ClockInWeekVO clockInWeekVO = new ClockInWeekVO();
        clockInWeekVO.setClockInUp(clockInUp);
        clockInWeekVO.setClockInDown(clockInDown);
        return ResultVO.success().setData(clockInWeekVO);
    }

    @Override
    public ResultVO getSexRatio(String storeId) {
        List<User> users = userMapper.selectList(new QueryWrapper<User>().eq("store_id", storeId));
        int man = 0;
        int woman = 0;
        for (User user : users) {
            if ("男".equals(user.getSex())) {
                man++;
            }
            if("女".equals(user.getSex())){
                woman++;
            }
        }
        HashMap<String, Integer> map = new HashMap<>();
        map.put("man", man);
        map.put("woman", woman);
        return ResultVO.success().setData(map);
    }
}
