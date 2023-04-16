package com.cj.personal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cj.common.en.CommonError;
import com.cj.common.entity.ClockIn;
import com.cj.common.exception.ClassException;
import com.cj.common.mapper.ClockInMapper;
import com.cj.common.utils.DateUtils;
import com.cj.common.utils.UUIDUtils;
import com.cj.common.vo.ResultVO;
import com.cj.personal.service.ClockInService;
import com.cj.personal.utils.LocationUtils;
import com.cj.personal.vo.GestureVO;
import com.cj.personal.vo.PlatPunch;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GlobalCoordinates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ClockInServiceImpl implements ClockInService {


    @Autowired
    private ClockInMapper clockInMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    /*
    活动定位打卡功能 */
    @Override
    public ResultVO getPunch(PlatPunch platPunch) {
        //1、设置目的地经度
        String longitudeS = PlatPunch.LONGITUDES;
        //设置目的纬度
        String latitudeS = PlatPunch.LATITUDES;
        // 2、由前端传过来的用户所在位置 经纬度
        String longitudeT = platPunch.getLongitude();
        String latitudeT = platPunch.getLatitude();
//        System.out.println(longitudeT + "=====" + latitudeT);
        // 3、对比
        GlobalCoordinates source = new GlobalCoordinates(Double.parseDouble(latitudeS), Double.parseDouble(longitudeS));
        GlobalCoordinates target = new GlobalCoordinates(Double.parseDouble(latitudeT), Double.parseDouble(longitudeT));
        //这是两种坐标系计算方法，这是第一种坐标系计算方法（我们用的这种）
        double geoCurve = LocationUtils.getDistanceMeter(source, target, Ellipsoid.Sphere);
        //这是两种坐标系计算方法，这是第二种坐标系计算方法
//        double geoCurve2 = LocationUtils.getDistanceMeter(source, target, Ellipsoid.WGS84);
        //如果用户和目的地位置大于100米，不能打卡；
        if (geoCurve < 100) {
            return ResultVO.fail().setMessage("不在范围内");
        }

        ClockIn clockIn = new ClockIn();
        clockIn.setId(UUIDUtils.getId());
        clockIn.setSignTime(DateUtils.getNowDate());
        clockIn.setSignType(platPunch.getSignType());
        clockIn.setInfo(platPunch.getInfo());
        clockIn.setEmployeeId(platPunch.getEmployeeId());
        clockIn.setStoreId(platPunch.getStoreId());
        int res = clockInMapper.insert(clockIn);
        if (res < 0) {
            ClassException.cast(CommonError.INSERT_ERROR);
        }
        return ResultVO.success();
    }

    @Override
    public ResultVO gestureClockIn(GestureVO gestureVO) {
        String realGesture = (String) redisTemplate.opsForValue().get(ClockIn.GESTURE);
        if (!gestureVO.getGesture().equals(realGesture)) {
            return ResultVO.fail().setMessage("签到码错误或过期");
        }
        //查询是否已经签到了
        ClockIn clockIn = clockInMapper.selectOne(new QueryWrapper<ClockIn>().apply("DATE(sign_time) = CURDATE()").eq("sign_type", gestureVO.getSignType()));
        if (clockIn != null) {
            return ResultVO.fail().setMessage("不能重复签到");
        }
        ClockIn clockInRes = new ClockIn();
        clockInRes.setId(UUIDUtils.getId());
        clockInRes.setSignTime(DateUtils.getNowDate());
        clockInRes.setSignType(gestureVO.getSignType());
        clockInRes.setInfo(gestureVO.getInfo());
        clockInRes.setEmployeeId(gestureVO.getEmployeeId());
        clockInRes.setStoreId(gestureVO.getStoreId());
        clockInMapper.insert(clockInRes);
        return ResultVO.success();
    }

    @Override
    public ResultVO isClockIn(String employeeId) {
        String today = DateUtils.getNowDate();
        today = today.substring(0, 10);
        // 构建查询条件
        List<ClockIn> cis = clockInMapper.selectList(new QueryWrapper<ClockIn>().eq("employee_id", employeeId));
        List<ClockIn> res = new ArrayList<>();
        for (ClockIn ci : cis) {
            if (ci.getSignTime().substring(0, 10).equals(today)){
                res.add(ci);
            }
        }
        return ResultVO.success().setData(res);
    }


}
