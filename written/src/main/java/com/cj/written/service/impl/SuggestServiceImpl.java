package com.cj.written.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cj.common.en.CommonError;
import com.cj.common.entity.Suggest;
import com.cj.common.exception.ClassException;
import com.cj.common.mapper.SuggestMapper;
import com.cj.common.utils.DateUtils;
import com.cj.common.utils.UUIDUtils;
import com.cj.common.vo.ResultVO;
import com.cj.written.service.SuggestService;
import com.cj.written.vo.SuggestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SuggestServiceImpl implements SuggestService {

    @Autowired
    private SuggestMapper suggestMapper;

    @Override
    public ResultVO getSuggestById(String id, String page) {
        //分页
        Page<Suggest> suggestPage = new Page<>(Integer.parseInt(page), 10);
        QueryWrapper<Suggest> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("employee_id", id);
        suggestMapper.selectPage(suggestPage, queryWrapper);
        Map<String, Object> map = new HashMap<>();
        map.put("suggests", suggestPage.getRecords());
        map.put("total", suggestPage.getTotal());

        return ResultVO.success().setData(map);
    }

    @Override
    public ResultVO submitSuggest(SuggestVO suggestVO) {

        Suggest suggest = new Suggest();

        suggest.setId(UUIDUtils.getId());
        suggest.setContent(suggestVO.getContent());
        suggest.setEmployeeId(suggestVO.getId());
        suggest.setSubTime(DateUtils.getNowDate());
        suggest.setStoreId(suggestVO.getStoreId());
        int res = suggestMapper.insert(suggest);
        if (res < 0) {
            ClassException.cast(CommonError.INSERT_ERROR);
        }
        return ResultVO.success();
    }

    @Override
    public ResultVO deleteSuggestById(String[] ids) {
        int res = suggestMapper.deleteBatchIds(Arrays.asList(ids));
        if (res < 0) {
            ClassException.cast(CommonError.DELETE_ERROR);
        }
        return ResultVO.success();
    }

    @Override
    public ResultVO getAllSuggest(String storeId) {
        List<Suggest> suggests = suggestMapper.selectList(new QueryWrapper<Suggest>().eq("store_id", storeId));
        return ResultVO.success().setData(suggests);
    }


}
