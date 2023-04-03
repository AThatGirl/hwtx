package com.cj.written.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cj.common.en.CommonError;
import com.cj.common.entity.Written;
import com.cj.common.exception.ClassException;
import com.cj.common.mapper.WrittenMapper;
import com.cj.common.utils.DateUtils;
import com.cj.common.utils.UUIDUtils;
import com.cj.common.vo.ResultVO;
import com.cj.written.service.WrittenService;
import com.cj.written.vo.WrittenUpdateVO;
import com.cj.written.vo.WrittenVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WrittenServiceImpl implements WrittenService {


    @Autowired
    private WrittenMapper writtenMapper;


    @Override
    public ResultVO writeWritten(WrittenVO writtenVO) {

        Written written = new Written();

        written.setId(UUIDUtils.getId());
        written.setReason(writtenVO.getReason());
        written.setStartTime(writtenVO.getStartTime());
        written.setEndTime(writtenVO.getEndTime());
        written.setStatus(Written.AUDIT_STATUS);
        written.setCreateTime(DateUtils.getNowDate());
        written.setUpdateTime(DateUtils.getNowDate());
        written.setEmployeeId(writtenVO.getId());
        written.setStoreId(writtenVO.getStoreId());

        int res = writtenMapper.insert(written);
        if (res < 0) {
            ClassException.cast(CommonError.INSERT_ERROR);
        }
        return ResultVO.success();
    }

    @Override
    public ResultVO deleteWritten(String[] ids) {
        int res = writtenMapper.deleteBatchIds(Arrays.asList(ids));
        if (res < 0) {
            ClassException.cast(CommonError.DELETE_ERROR);
        }
        return ResultVO.success();
    }

    @Override
    public ResultVO getWrittenById(String id, String page) {

        //分页
        Page<Written> writtenPage = new Page<>(Integer.parseInt(page), 10);
        QueryWrapper<Written> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(id)) {
            queryWrapper.eq("employee_id", id);
        } else {
            queryWrapper = null;
        }
        writtenMapper.selectPage(writtenPage,queryWrapper);
        Map<String, Object> map = new HashMap<>();
        map.put("writtens", writtenPage.getRecords());
        map.put("total", writtenPage.getTotal());
        return ResultVO.success().setData(map);
    }

    @Override
    public ResultVO updateWrittenById(WrittenUpdateVO writtenUpdateVO) {
        //先根据id查询请假条
        Written written = writtenMapper.selectById(writtenUpdateVO.getId());
        written.setReason(writtenUpdateVO.getReason());
        written.setStartTime(writtenUpdateVO.getStartTime());
        written.setEndTime(writtenUpdateVO.getEndTime());
        written.setUpdateTime(DateUtils.getNowDate());

        int res = writtenMapper.updateById(written);
        if(res < 0){
            ClassException.cast(CommonError.UPDATE_ERROR);
        }
        return ResultVO.success();
    }


}
