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
import com.cj.personal.vo.PlatPunch;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GlobalCoordinates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ClockInServiceImpl implements ClockInService {


    @Autowired
    private ClockInMapper clockInMapper;


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
        int res = clockInMapper.insert(clockIn);
        if (res < 0){
            ClassException.cast(CommonError.INSERT_ERROR);
        }
        return ResultVO.success().setMessage("打卡成功").setData(clockIn);

    }


}
