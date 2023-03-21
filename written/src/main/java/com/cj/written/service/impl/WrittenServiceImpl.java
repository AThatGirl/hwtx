package com.cj.written.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cj.common.en.CommonError;
import com.cj.common.entity.Written;
import com.cj.common.exception.ClassException;
import com.cj.common.mapper.WrittenMapper;
import com.cj.common.utils.DateUtils;
import com.cj.common.utils.UUIDUtils;
import com.cj.common.vo.ResultVO;
import com.cj.written.service.WrittenService;
import com.cj.written.vo.WrittenVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

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

        int res = writtenMapper.insert(written);
        if (res < 0) {
            ClassException.cast(CommonError.INSERT_ERROR);
        }


        return ResultVO.success().setMessage("提交成功");
    }

    @Override
    public ResultVO deleteWritten(String[] ids) {
        int res = writtenMapper.deleteBatchIds(Arrays.asList(ids));
        if (res < 0) {
            ClassException.cast(CommonError.DELETE_ERROR);
        }

        return ResultVO.success().setMessage("删除成功");
    }

    @Override
    public ResultVO getWrittenById(String id) {
        QueryWrapper<Written> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(id)) {
            queryWrapper.eq("employee_id", id);
        } else {
            queryWrapper = null;
        }
        List<Written> writtens = writtenMapper.selectList(queryWrapper);
        return ResultVO.success().setData(writtens);
    }


}
