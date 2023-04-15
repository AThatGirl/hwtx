package com.cj.manage.vo;

import com.cj.common.entity.ClockIn;
import com.cj.common.entity.User;
import lombok.Data;

@Data
public class ClockInResponseVO {

    private ClockIn clockIn;
    private User user;

}
