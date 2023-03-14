package com.cj.personal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cj.common.entity.Preference;
import com.cj.common.mapper.PreferenceMapper;
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
        Preference preference = null;
        try {
            preference = preferenceMapper.selectOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.fail();
        }
        return ResultVO.success().setData(preference);
    }

    @Override
    public ResultVO changePrefer(Preference preference) {

        try {
            preferenceMapper.updateById(preference);
        }catch (Exception e){
            e.printStackTrace();
            return ResultVO.fail();
        }
        return ResultVO.success();
    }
}
