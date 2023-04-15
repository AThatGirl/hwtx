package com.cj.personal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cj.common.en.CommonError;
import com.cj.common.entity.Preference;
import com.cj.common.exception.ClassException;
import com.cj.common.mapper.PreferenceMapper;
import com.cj.common.utils.UUIDUtils;
import com.cj.common.vo.ResultVO;
import com.cj.personal.service.PreferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PreferServiceImpl implements PreferService {

    @Autowired
    private PreferenceMapper preferenceMapper;

    @Override
    public ResultVO getPrefer(String id) {
        QueryWrapper<Preference> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("employee_id", id);
        Preference preference = preferenceMapper.selectOne(queryWrapper);
        return ResultVO.success().setData(preference);
    }

    @Override
    public ResultVO changePrefer(Preference preference) {
        int res = preferenceMapper.updateById(preference);
        if (res < 0) {
            ClassException.cast(CommonError.UPDATE_ERROR);
        }
        return ResultVO.success();
    }

    @Override
    public ResultVO addPrefer(Preference preference) {
        preference.setId(UUIDUtils.getId());
        int res = preferenceMapper.insert(preference);
        if (res < 0){
            ClassException.cast(CommonError.INSERT_ERROR);
        }
        return ResultVO.success();
    }

    @Override
    public Preference selectByEmployee(String id) {
        QueryWrapper<Preference> wrapper=new QueryWrapper<>();
        wrapper.eq("employee_id",id);
        return preferenceMapper.selectOne(wrapper);
    }
}
