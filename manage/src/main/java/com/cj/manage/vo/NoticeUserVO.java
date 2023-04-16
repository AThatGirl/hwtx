package com.cj.manage.vo;

import com.cj.common.entity.Notice;
import com.cj.common.entity.User;
import lombok.Data;

@Data
public class NoticeUserVO {

    private Notice notice;
    private User user;

}
