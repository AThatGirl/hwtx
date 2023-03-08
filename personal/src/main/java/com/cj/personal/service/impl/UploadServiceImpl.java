package com.cj.personal.service.impl;

import com.cj.common.entity.User;
import com.cj.common.mapper.UserMapper;
import com.cj.common.vo.ResultVO;
import com.cj.personal.service.UploadService;
import com.cj.personal.utils.OSSClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    private OSSClientUtils ossClientUtils;

    @Autowired
    private UserMapper userMapper;

    @Override
    public ResultVO avatarUpload(String id, MultipartFile file) {
        try {
            //初始化OSS对象
            ossClientUtils.initClient();
            //上传头像
            String fileUrl = ossClientUtils.uploadFile(file);

            User user = userMapper.selectById(id);
            user.setAvatar(fileUrl);
            //更新数据库
            userMapper.updateById(user);
            return ResultVO.success().setMessage("上传成功").setData(fileUrl);
        } catch (IOException e) {
            return ResultVO.fail().setMessage("上传失败");
        } finally {
            ossClientUtils.closeClient();
        }

    }
}
