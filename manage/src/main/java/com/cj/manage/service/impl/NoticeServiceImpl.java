package com.cj.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cj.common.en.CommonError;
import com.cj.common.entity.Notice;
import com.cj.common.entity.User;
import com.cj.common.exception.ClassException;
import com.cj.common.mapper.NoticeMapper;
import com.cj.common.mapper.UserMapper;
import com.cj.common.vo.ResultVO;
import com.cj.common.vo.SendNoticeVO;
import com.cj.manage.feign.MqServiceFeignClient;
import com.cj.manage.service.NoticeService;
import com.cj.manage.vo.NoticeUserVO;
import com.cj.manage.vo.NoticeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private MqServiceFeignClient mqServiceFeignClient;

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private UserMapper userMapper;

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
        //条件查询
        queryWrapper.and(w->{
            if (!StringUtils.isEmpty(noticeVO.getStatus())) {
                w.eq("status", noticeVO.getStatus());
            }
            if (!StringUtils.isEmpty(noticeVO.getStoreId())) {
                w.eq("store_id", noticeVO.getStoreId());
            }
            return w;
        });

        queryWrapper.or().eq("receiver_id", Notice.ALL_USER);
        noticeMapper.selectPage(noticePage, queryWrapper);

        //放入结果集
        Map<String, Object> map = new HashMap<>();
        List<Notice> records = noticePage.getRecords();
        List<NoticeUserVO> nuvs = new ArrayList<>();
        for (Notice record : records) {
            if (!StringUtils.isEmpty(noticeVO.getTime())) {
                if (isDate1BeforeDate2(noticeVO.getTime(), record.getCreateTime())) {
                    User user = userMapper.selectById(record.getReceiverId());
                    NoticeUserVO nuv = new NoticeUserVO();
                    nuv.setNotice(record);
                    nuv.setUser(user);
                    nuvs.add(nuv);
                }
            }else {
                User user = userMapper.selectById(record.getReceiverId());
                NoticeUserVO nuv = new NoticeUserVO();
                nuv.setNotice(record);
                nuv.setUser(user);
                nuvs.add(nuv);
            }

        }
        map.put("notices", nuvs);
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

    public boolean isDate1BeforeDate2(String date1, String date2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = sdf.parse(date1);
            Date d2 = sdf.parse(date2);
            return d1.before(d2);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }


}
