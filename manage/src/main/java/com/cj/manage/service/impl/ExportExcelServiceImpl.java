package com.cj.manage.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cj.common.entity.Notice;
import com.cj.common.entity.Suggest;
import com.cj.common.entity.User;
import com.cj.common.entity.Written;
import com.cj.common.mapper.*;
import com.cj.manage.service.ExportExcelService;
import com.cj.manage.vo.NoticeExcel;
import com.cj.manage.vo.SuggestExcel;
import com.cj.manage.vo.UserInfoExcel;
import com.cj.manage.vo.WrittenExcel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


@Service
public class ExportExcelServiceImpl implements ExportExcelService {

    @Autowired
    private StoreMapper storeMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WrittenMapper writtenMapper;

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private SuggestMapper suggestMapper;

    @Override
    public void writeUserToExcel(String storeId,HttpServletResponse response) {
        List<User> users = userMapper.selectList(new QueryWrapper<User>().eq("store_id", storeId));
        List<UserInfoExcel> dataList = new ArrayList<>();
        for (User user : users) {
            UserInfoExcel userInfoExcel = new UserInfoExcel();
            userInfoExcel.setName(user.getName());
            userInfoExcel.setSex(user.getSex());
            userInfoExcel.setAge(user.getAge());
            userInfoExcel.setPhone(user.getPhone());
            userInfoExcel.setBirthday(user.getBirthday());
            userInfoExcel.setEmail(user.getEmail());
            userInfoExcel.setCareer(user.getCareer());
            userInfoExcel.setStoreName(storeMapper.selectById(user.getStoreId()).getName());
            dataList.add(userInfoExcel);
        }
        try {
            downloadUnfilledToXlsx("用户信息表", response, UserInfoExcel.class, dataList);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void writeWrittenToExcel(String storeId, HttpServletResponse response) {
        List<Written> writtens = writtenMapper.selectList(new QueryWrapper<Written>().eq("store_id", storeId));
        List<WrittenExcel> dataList = new ArrayList<>();
        for (Written written : writtens) {
            WrittenExcel writtenExcel = new WrittenExcel();
            BeanUtils.copyProperties(written, writtenExcel);
            writtenExcel.setEmployeeName(userMapper.selectById(written.getEmployeeId()).getName());
            writtenExcel.setStoreName(storeMapper.selectById(written.getStoreId()).getName());
            dataList.add(writtenExcel);
        }
        try {
            downloadUnfilledToXlsx("请假信息表", response, WrittenExcel.class, dataList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeNoticeToExcel(String storeId, HttpServletResponse response) {
        List<Notice> notices = noticeMapper.selectList(new QueryWrapper<Notice>().eq("store_id", storeId));
        List<NoticeExcel> dataList = new ArrayList<>();
        for (Notice notice : notices) {
            NoticeExcel noticeExcel = new NoticeExcel();
            noticeExcel.setSenderName(userMapper.selectById(notice.getSenderId()).getName());
            noticeExcel.setReceiverName(notice.getReceiverId().equals(Notice.ALL_USER) ? userMapper.selectById(notice.getReceiverId()).getName() : Notice.ALL_USER);
            noticeExcel.setContent(notice.getContent());
            noticeExcel.setCreateTime(notice.getCreateTime());
            noticeExcel.setStoreName(storeMapper.selectById(notice.getStoreId()).getName());
            dataList.add(noticeExcel);
        }
        try {
            downloadUnfilledToXlsx("通知信息表", response, NoticeExcel.class, dataList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeSuggestToExcel(String storeId, HttpServletResponse response) {
        List<Suggest> suggests = suggestMapper.selectList(new QueryWrapper<Suggest>().eq("store_id", storeId));
        List<SuggestExcel> dataList = new ArrayList<>();
        for (Suggest suggest : suggests) {
            SuggestExcel suggestExcel = new SuggestExcel();
            suggestExcel.setContent(suggest.getContent());
            suggestExcel.setEmployeeName(userMapper.selectById(suggest.getEmployeeId()).getName());
            suggestExcel.setSubTime(suggest.getSubTime());
            suggestExcel.setStoreName(storeMapper.selectById(suggest.getStoreId()).getName());
            dataList.add(suggestExcel);
        }
        try {
            downloadUnfilledToXlsx("建议信息表", response, SuggestExcel.class, dataList);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //无填充方式导出并下载xlsx
    public static void downloadUnfilledToXlsx(String excelName, HttpServletResponse response, Class cla, List list) throws IOException {
        // 这里注意 使用swagger 会导致各种问题，easyexcel官方文档推荐直接用浏览器或者用postman测试
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode(excelName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), cla).sheet(excelName).doWrite(list);
    }


}
