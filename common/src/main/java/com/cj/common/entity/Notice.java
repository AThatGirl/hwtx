package com.cj.common.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 消息
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Notice {

    private static final long serialVersionUID = 1L;

    public static final String UNREAD_STATUS = "未读";
    public static final String READ_STATUS = "已读";
    public static final String EXPIRE_STATUS = "过期";
    public static final String DELETE_STATUS = "删除";

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    private String senderId;

    private String receiverId;

    private String content;

    private String status;

    private String createTime;

}
