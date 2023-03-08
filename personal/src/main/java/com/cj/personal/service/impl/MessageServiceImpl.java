package com.cj.personal.service.impl;

import com.cj.common.entity.User;
import com.cj.common.mapper.UserMapper;
import com.cj.common.vo.ResultVO;
import com.cj.personal.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ResultVO getMessageById(String id) {
        User user = null;
        try {
            user = userMapper.selectById(id);
        }catch (Exception e){
            e.printStackTrace();
            return ResultVO.fail().setMessage("获取失败");
        }

        return ResultVO.success().setData(user);
    }

    @Override
    public ResultVO updateMessage(User user) {
        try {
            userMapper.updateById(user);
        }catch (Exception e){
            e.printStackTrace();
            return ResultVO.fail().setMessage("更新失败");
        }

        return ResultVO.success().setMessage("更新成功");
    }
}
