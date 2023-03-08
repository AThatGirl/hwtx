package com.cj.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cj.common.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jerry
 * @since 2023-01-10
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
