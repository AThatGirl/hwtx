package com.cj.care.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cj.care.service.CareService;
import com.cj.care.vo.CareGetVO;
import com.cj.common.en.CommonError;
import com.cj.common.entity.CareRecord;
import com.cj.common.exception.ClassException;
import com.cj.common.mapper.CareApplicationMapper;
import com.cj.common.mapper.CareRecordMapper;
import com.cj.common.utils.DateUtils;
import com.cj.common.utils.UUIDUtils;
import com.cj.common.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class CareServiceImpl implements CareService {


    @Autowired
    private CareApplicationMapper careApplicationMapper;

    @Autowired
    private CareRecordMapper careRecordMapper;

    @Override
    public <T> ResultVO getCare(CareGetVO careGetVO, BaseMapper<T> careMapper) {
        // 构建查询条件
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("store_id", careGetVO.getStoreId());
        if (!StringUtils.isEmpty(careGetVO.getUsername())) {
            queryWrapper.eq("username", careGetVO.getUsername());
        }
        // 构建分页对象
        Page<T> pageObj = new Page<>(StringUtils.isEmpty(careGetVO.getPage()) ? 1 : Integer.parseInt(careGetVO.getPage()), 10);
        // 执行查询
        careMapper.selectPage(pageObj, queryWrapper);
        // 打印结果
        Map<String, Object> map = new HashMap<>();
        map.put("total", pageObj.getTotal());
        map.put("data", pageObj.getRecords());
        return ResultVO.success().setData(map);
    }

    @Override
    public ResultVO addCareRecord(CareRecord careRecord) {
        careRecord.setId(UUIDUtils.getId());
        careRecord.setTime(DateUtils.getNowDate());
        int res = careRecordMapper.insert(careRecord);
        if (res < 0){
            ClassException.cast(CommonError.INSERT_ERROR);
        }
        return ResultVO.success();
    }

    @Override
    public <T> ResultVO delCare(String[] ids, BaseMapper<T> careMapper) {
        int res = careMapper.deleteBatchIds(Arrays.asList(ids));
        if (res < 0){
            ClassException.cast(CommonError.DELETE_ERROR);
        }
        return ResultVO.success();
    }

}
