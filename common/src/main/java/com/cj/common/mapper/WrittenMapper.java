package com.cj.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cj.common.entity.Written;
import com.cj.common.vo.WrittenResponseVO;
import com.cj.common.vo.WrittenSearchVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jerry
 * @since 2023-01-10
 */
@Mapper
public interface WrittenMapper extends BaseMapper<Written> {

    List<WrittenResponseVO> searchWrittens(WrittenSearchVO writtenSearchVO);

    int getTotalNum(WrittenSearchVO writtenSearchVO);

}
