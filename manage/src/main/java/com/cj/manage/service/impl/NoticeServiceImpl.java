package com.cj.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cj.common.en.CommonError;
import com.cj.common.entity.Notice;
import com.cj.common.exception.ClassException;
import com.cj.common.mapper.NoticeMapper;
import com.cj.common.vo.ResultVO;
import com.cj.common.vo.SendNoticeVO;
import com.cj.manage.feign.MqServiceFeignClient;
import com.cj.manage.service.NoticeService;
import com.cj.manage.vo.NoticeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private MqServiceFeignClient mqServiceFeignClient;

    @Autowired
    private NoticeMapper noticeMapper;

    @Override
    public ResultVO sendNotice(SendNoticeVO sendNoticeVO) {
        return mqServiceFeignClient.sendNotice(sendNoticeVO);
    }

    @Override
    public ResultVO getNoticeForPage(NoticeVO noticeVO) {
        //分页
        Page<Notice> noticePage = new Page<>(Integer.parseInt(noticeVO.getPage()), 10);
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        //全体成员的通知也要包括
        queryWrapper.or().eq("receiver_id", Notice.ALL_USER).eq("receiver_id", noticeVO.getId());
        //条件查询
        if (!StringUtils.isEmpty(noticeVO.getStatus())) {
            queryWrapper.eq("status", noticeVO.getStatus());
        }
        if (!StringUtils.isEmpty(noticeVO.getStoreId())) {
            queryWrapper.eq("store_id", noticeVO.getStoreId());
        }
        if (!StringUtils.isEmpty(noticeVO.getTime())) {
            queryWrapper.ge("create_time", noticeVO.getTime());
        }
        noticeMapper.selectPage(noticePage, queryWrapper);

        //放入结果集
        Map<String, Object> map = new HashMap<>();
        map.put("notices", noticePage.getRecords());
        map.put("total", noticePage.getTotal());
        return ResultVO.success().setData(map);
    }

    @Override
    public ResultVO deleteNotice(String[] ids) {
        int res = noticeMapper.deleteBatchIds(Arrays.asList(ids));
        if (res < 0) {
            ClassException.cast(CommonError.DELETE_ERROR);
        }
        return ResultVO.success();
    }

    @Override
    public ResultVO readNotice(String id) {

        Notice notice = noticeMapper.selectById(id);
        notice.setStatus(Notice.READ_STATUS);
        int res = noticeMapper.updateById(notice);
        if (res < 0) {
            ClassException.cast(CommonError.UPDATE_ERROR);
        }
        return ResultVO.success();
    }


}
