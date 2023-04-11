package com.cj.care.service;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cj.care.vo.CareGetVO;
import com.cj.common.entity.CareRecord;
import com.cj.common.vo.ResultVO;

/**
 * 关怀服务
 */
public interface CareService {


    /**
     * 条件查询关怀申请
     * @param careGetVO
     * @return
     */
    <T> ResultVO getCare(CareGetVO careGetVO, BaseMapper<T> careMapper);

    /**
     * 添加关怀记录
     * @param careRecord
     * @return
     */
    ResultVO addCareRecord(CareRecord careRecord);

    /**
     * 删除关怀
     * @param ids
     * @param careMapper
     * @param <T>
     * @return
     */
    <T> ResultVO delCare(String[] ids, BaseMapper<T> careMapper);


}
