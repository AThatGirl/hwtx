package com.schedule.service_schedule.entity.vo.client;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author jerry
 * @since 2023-01-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final Integer NO_PASS = 0;
    public static final Integer PASSING = 1;
    public static final Integer YES_PASS = 2;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    private String name;

    private Integer age;

    private String sex;

    private String birthday;

    private String phone;

    private String password;

    private String avatar;

    private String career;

    private String email;

    private String storeId;

    private Integer pass;

    private Preference preference;

    private double dayWorkTime;

    private double weekWorkTime;

}
