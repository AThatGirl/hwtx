package com.cj.personal.service.impl;

import com.cj.common.en.CommonError;
import com.cj.common.entity.User;
import com.cj.common.exception.ClassException;
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
        User user = userMapper.selectById(id);
        return ResultVO.success().setData(user);
    }

    @Override
    public ResultVO updateMessage(User user) {
        int res = userMapper.updateById(user);
        if (res < 0){
            ClassException.cast(CommonError.UPDATE_ERROR);
        }
        return ResultVO.success().setMessage("更新成功");
    }
}
