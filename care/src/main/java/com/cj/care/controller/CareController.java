package com.cj.care.controller;


import com.cj.care.service.CareService;
import com.cj.care.vo.CareGetVO;
import com.cj.common.entity.CareApplication;
import com.cj.common.entity.CareRecord;
import com.cj.common.mapper.CareApplicationMapper;
import com.cj.common.mapper.CareFeedbackMapper;
import com.cj.common.mapper.CareRecordMapper;
import com.cj.common.vo.ResultVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/care/care")
@Api("员工关怀")
public class CareController {

    @Autowired
    private CareService careService;
    @Autowired
    private CareApplicationMapper careApplicationMapper;
    @Autowired
    private CareRecordMapper careRecordMapper;
    @Autowired
    private CareFeedbackMapper careFeedbackMapper;

    @PostMapping("/getCareApplication")
    public ResultVO getCareApplication(@RequestBody CareGetVO careGetVO) {
        return careService.getCare(careGetVO, careApplicationMapper);
    }

    @PostMapping("/getCareRecord")
    public ResultVO getCareRecord(@RequestBody CareGetVO careGetVO) {
        return careService.getCare(careGetVO, careRecordMapper);
    }

    @PostMapping("/getCareFeedback")
    public ResultVO getCareFeedback(@RequestBody CareGetVO careGetVO) {
        return careService.getCare(careGetVO, careFeedbackMapper);
    }

    @PostMapping("/addCareRecord")
    public ResultVO addCareRecord(@RequestBody CareRecord careRecord){
        return careService.addCareRecord(careRecord);
    }

    @PostMapping("/delCareRecord")
    public ResultVO delCareRecord(@RequestBody String[] ids){
        return careService.delCare(ids, careRecordMapper);
    }

    @PostMapping("/delCareApplication")
    public ResultVO delCareApplication(@RequestBody String[] ids){
        return careService.delCare(ids, careApplicationMapper);
    }

    @PostMapping("/delCareFeedback")
    public ResultVO delCareFeedback(@RequestBody String[] ids){
        return careService.delCare(ids, careFeedbackMapper);
    }

}
