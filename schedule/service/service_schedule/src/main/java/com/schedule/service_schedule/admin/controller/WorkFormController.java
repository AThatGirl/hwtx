package com.schedule.service_schedule.admin.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.schedule.common.utils.ResultVO;
import com.schedule.service_base.handler.exceptionHandler.ScheduleException;
import com.schedule.service_schedule.client.EmployeeClient;
import com.schedule.service_schedule.client.WrittenClient;
import com.schedule.service_schedule.entity.Rule;
import com.schedule.service_schedule.entity.WorkForm;
import com.schedule.service_schedule.entity.dto.PassengerFlowExcelDto;
import com.schedule.service_schedule.entity.dto.ShiftDto;
import com.schedule.service_schedule.entity.vo.client.Preference;
import com.schedule.service_schedule.entity.vo.client.User;
import com.schedule.service_schedule.entity.vo.excel.ShiftExcelVo;
import com.schedule.service_schedule.entity.vo.workForm.ShiftVo;
import com.schedule.service_schedule.entity.vo.workForm.WorkFormVo;
import com.schedule.service_schedule.listener.PassengerFlowExcelListener;
import com.schedule.service_schedule.admin.service.RuleService;
import com.schedule.service_schedule.admin.service.WorkFormService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  班次接口
 * </p>
 *
 * @author tpt
 * @since 2023-02-17
 */
@RestController
@RequestMapping("/service_schedule/admin/work-form")
@Api(tags = "排班接口")
public class WorkFormController {
    @Resource
    private WorkFormService workFormService;

    @Resource
    private RuleService ruleService;

    @Resource
    private EmployeeClient employeeClient;

    @Resource
    WrittenClient writtenClient;

    @PostMapping("/generateShifts/{id}/{size}")
    @ApiOperation(value = "上传客流量excel文件,生成排班班次")
    public ResultVO insertExcelFile(@RequestPart MultipartFile file, @PathVariable(value = "id") String id,
                                    @PathVariable double size){
        try {
            //1 获取文件输入流
            InputStream inputStream = null;
            inputStream = file.getInputStream();
            PassengerFlowExcelListener passengerFlowExcelListener=new PassengerFlowExcelListener();
            // 这里 需要指定读用哪个class去读，然后读取所有的sheet 文件流会自动关闭
            EasyExcel.read(inputStream, PassengerFlowExcelDto.class,
                    passengerFlowExcelListener
            ).doReadAll();
            //获取客流量数据
            List<List<PassengerFlowExcelDto>> passengerFlowExcelVoLists = passengerFlowExcelListener.getPassengerFlowExcelVoLists();
            /*passengerFlowExcelVoLists.forEach(p->{
                p.forEach(l->{
                    log.info(l.toString());
                });
            });*/
            //通过 客流量数据,规则，门店面积 生成排班班次
            Map<String,String> ruleMap=new HashMap<>();
            QueryWrapper<Rule> wrapper=new QueryWrapper<>();
            List<String> list=new ArrayList<>();
            list.add("班次限制规则");
            list.add("工作时长规则");
            list.add("客流量规则");
            list.add("无客流量规则");
            list.add("关店规则");
            list.add("客流量规则");
            list.add("开店规则");
            list.add("门店营业时间规则");
            wrapper.in("type",list);
            wrapper.eq("store_id",id);
            List<Rule> ruleList = ruleService.list(wrapper);
            for(Rule rule:ruleList){
                ruleMap.put(rule.getType(),rule.getValue());
            }
            workFormService.insertWorkForm(passengerFlowExcelVoLists,ruleMap,size,id);

        } catch (IOException e) {
            throw new ScheduleException(20001,"获取排班失败");
        }
        return ResultVO.success();
    }

    @GetMapping(value = "/getWeekShifts/{storeId}/{startDate}/{endDate}")
    @ApiOperation(value = "获取一周的班次信息")
    public ResultVO getWeekShift(@PathVariable String storeId,
                                 @PathVariable LocalDate startDate,
                                 @PathVariable LocalDate endDate,
                                 @RequestParam(required = false) String position,
                                 @RequestParam(required = false) String employeeName){
        List<List<WorkForm>> list=workFormService.getWeekShift(storeId, startDate, endDate,position,employeeName);
        int count=0;
        for(List<WorkForm> workFormList:list){
            for(WorkForm workForm:workFormList){
                if(workForm.getEmployeeId()==null||workForm.getEmployeeId().equals("")){
                    count++;
                }
            }
        }
        return ResultVO.success().data("weekShiftList",list).data("count",count);
    }
    @GetMapping("/getDayShifts/{storeId}/{date}")
    @ApiOperation(value = "获取一天的班次信息")
    public ResultVO getDayShift(@PathVariable String storeId,@PathVariable LocalDate date,
                                @RequestParam(required = false) String position,
                                @RequestParam(required = false) String employeeName){
        List<WorkForm> list=workFormService.getDayShift(storeId,date,position,employeeName);
        return ResultVO.success().data("dayShiftList",list);
    }

    @PutMapping("/scheduleShift/{storeId}/{startDate}/{endDate}")
    @ApiOperation(value = "班次排班")
    public ResultVO scheduleShift(@PathVariable String storeId,@PathVariable LocalDate startDate,@PathVariable LocalDate endDate){
        //给员工安排班次
        List<List<List<WorkForm>>> shifts=workFormService.scheduleShifts(storeId,startDate,endDate);
        //更新班次员工
        workFormService.updateById(shifts);
        return ResultVO.success();
    }

    @PostMapping("/getShift-ConformEmployee")
    @ApiOperation(value = "获取班次符合的员工")
    public ResultVO getShiftConformEmployee(@RequestBody ShiftVo shiftVo){
        List<User> userList=workFormService.getConformEmployee(shiftVo);
        return ResultVO.success().data("userList",userList);
    }

    @PutMapping("/updateShiftEmployee/{employeeId}/{shiftId}/{allowCareer}")
    @ApiOperation(value = "修改班次的员工")
    public ResultVO updateShiftEmployee(@PathVariable String employeeId,@PathVariable String shiftId,
                                        @PathVariable String allowCareer){
        workFormService.updateById(new WorkForm().setEmployeeId(employeeId).setId(shiftId).setAllowCareer(allowCareer));
        return ResultVO.success();
    }

    @PostMapping("/insertShift")
    @ApiOperation(value = "添加班次")
    public ResultVO insertShift(@ApiParam(value = "班次信息") @RequestBody WorkFormVo workFormVo){
        WorkForm workForm=new WorkForm();
        BeanUtils.copyProperties(workFormVo,workForm);
        workFormService.save(workForm);

        return ResultVO.success();
    }

    @GetMapping("/getShiftsExcel/{storeId}/{startDate}/{endDate}")
    @ApiOperation("获取一周的班次excel文件")
    public void getShiftsExcel(@PathVariable String storeId, @PathVariable LocalDate startDate, @PathVariable LocalDate endDate,HttpServletResponse response){
        String fileName=startDate.toString()+"~"+endDate.toString()+"班次";
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(response.getOutputStream()).build();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<List<ShiftExcelVo>> shiftExcelVos=null;
        shiftExcelVos=workFormService.getShiftExcelInfo(storeId,startDate,endDate);
        for(List<ShiftExcelVo> shiftExcelVoList:shiftExcelVos){
            //这里 需要指定写用哪个class去写
            WriteSheet writeSheet = EasyExcel.writerSheet(shiftExcelVos.indexOf(shiftExcelVoList), shiftExcelVoList.get(0).getDate()).head(ShiftExcelVo.class).build();
            excelWriter.write(shiftExcelVoList, writeSheet);
        }

        //千万别忘记finish 会帮忙关闭流
        excelWriter.finish();
    }


    @DeleteMapping("/delShift/{shiftId}")
    @ApiOperation("删除班次")
    public ResultVO delShift(@PathVariable String shiftId){
        workFormService.removeById(shiftId);
        return ResultVO.success();
    }
    /*@GetMapping("/getEmployee/{employeeId}")
    public ResultVO get(@PathVariable String employeeId){
        return ResultVO.success().data("user",writtenClient.getWrittenByEmployeeId(employeeId));
    }*/

    @GetMapping("/getMonthShifts/{storeId}/{startDate}/{endDate}")
    @ApiOperation("获得月排班班次")
    public ResultVO getMonthShift(@PathVariable String storeId,
                                  @PathVariable LocalDate startDate,
                                  @PathVariable LocalDate endDate){
        List<List<WorkForm>> lists=workFormService.getMonthShifts(storeId,startDate,endDate);
        return ResultVO.success().data("monthShifts",lists);
    }

    @GetMapping("/getEmployeeWorkHour/{employeeId}/{date}/{startDate}/{endDate}")
    public ResultVO getEmployeeWorkHour(@PathVariable String employeeId,
                                        @PathVariable LocalDate date,
                                        @PathVariable LocalDate startDate,
                                        @PathVariable LocalDate endDate){
        return ResultVO.success().data("user",workFormService.getEmployeeWorkHour(employeeId,date,startDate,endDate));
    }


}

