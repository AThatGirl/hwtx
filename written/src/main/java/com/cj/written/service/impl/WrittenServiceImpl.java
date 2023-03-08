package com.cj.written.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cj.common.entity.Written;
import com.cj.common.mapper.WrittenMapper;
import com.cj.common.utils.DateUtils;
import com.cj.common.utils.UUIDUtils;
import com.cj.common.vo.ResultVO;
import com.cj.written.service.WrittenService;
import com.cj.written.vo.WrittenVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

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
        try {
            writtenMapper.insert(written);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.fail().setMessage("提交失败");
        }

        return ResultVO.success().setMessage("提交成功");
    }

    @Override
    public ResultVO deleteWritten(String[] ids) {

        try {
            writtenMapper.deleteBatchIds(Arrays.asList(ids));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.fail().setMessage("删除失败");
        }

        return ResultVO.success().setMessage("删除成功");
    }

    @Override
    public ResultVO getWrittenById(String id) {
        QueryWrapper<Written> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("employee_id", id);
        Written written = writtenMapper.selectOne(queryWrapper);
        return ResultVO.success().setData(written);
    }


}
