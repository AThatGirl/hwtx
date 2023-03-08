package com.cj.written.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cj.common.entity.Suggest;
import com.cj.common.mapper.SuggestMapper;
import com.cj.common.utils.UUIDUtils;
import com.cj.common.vo.ResultVO;
import com.cj.written.service.SuggestService;
import com.cj.written.vo.SuggestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuggestServiceImpl implements SuggestService {

    @Autowired
    private SuggestMapper suggestMapper;

    @Override
    public ResultVO getSuggestById(String id) {
        QueryWrapper<Suggest> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("employee_id", id);
        List<Suggest> suggests = null;
        try {
            suggests = suggestMapper.selectList(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.fail().setMessage("获取失败");
        }
        return ResultVO.success().setMessage(suggests.size() + "条建议").setData(suggests);
    }

    @Override
    public ResultVO submitSuggest(SuggestVO suggestVO) {

        Suggest suggest = new Suggest();

        suggest.setId(UUIDUtils.getId());
        suggest.setContent(suggestVO.getContent());
        suggest.setEmployeeId(suggestVO.getId());
        suggest.setSubTime(suggestVO.getSubmitTime());

        try {
            suggestMapper.insert(suggest);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.fail().setMessage("提交异常");
        }

        return ResultVO.success().setMessage("提交成功");
    }

    @Override
    public ResultVO deleteSuggestById(String id) {
        try {
            suggestMapper.deleteById(id);

        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.fail().setMessage("删除失败");
        }
        return ResultVO.success().setMessage("删除成功");
    }


}
