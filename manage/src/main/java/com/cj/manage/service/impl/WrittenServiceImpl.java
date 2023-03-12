package com.cj.manage.service.impl;

import com.cj.common.entity.Written;
import com.cj.common.mapper.WrittenMapper;
import com.cj.common.vo.ResultVO;
import com.cj.common.vo.WrittenResponseVO;
import com.cj.common.vo.WrittenSearchVO;
import com.cj.manage.service.WrittenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WrittenServiceImpl implements WrittenService {

    @Autowired
    private WrittenMapper writtenMapper;

    @Override
    public ResultVO search(WrittenSearchVO writtenSearchVO) {
        writtenSearchVO.setPageNum((writtenSearchVO.getPageNum() - 1) * 10);
        List<WrittenResponseVO> writtens = writtenMapper.searchWrittens(writtenSearchVO);
        Map<String, Object> map = new HashMap<>();
        map.put("total", writtenMapper.getTotalNum(writtenSearchVO));
        map.put("writtens", writtens);
        return ResultVO.success().setData(map);
    }

    @Override
    public ResultVO examine(String id, String status) {
        Written written = writtenMapper.selectById(id);
        written.setStatus(status);

        try {
            writtenMapper.updateById(written);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.fail().setMessage("更新失败");
        }
        return ResultVO.success();
    }


}
