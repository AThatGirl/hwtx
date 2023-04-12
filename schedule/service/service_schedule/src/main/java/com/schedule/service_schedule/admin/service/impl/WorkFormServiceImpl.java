package com.schedule.service_schedule.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.schedule.service_base.handler.exceptionHandler.ScheduleException;
import com.schedule.service_schedule.admin.service.RuleService;
import com.schedule.service_schedule.client.EmployeeClient;
import com.schedule.service_schedule.entity.Rule;
import com.schedule.service_schedule.entity.WorkForm;
import com.schedule.service_schedule.entity.dto.EmployeeDto;
import com.schedule.service_schedule.entity.dto.WorkFormDto;
import com.schedule.service_schedule.entity.dto.PassengerFlowExcelDto;
import com.schedule.service_schedule.entity.dto.ShiftDto;
import com.schedule.service_schedule.entity.vo.client.Preference;
import com.schedule.service_schedule.entity.vo.client.User;
import com.schedule.service_schedule.entity.vo.excel.ShiftExcelVo;
import com.schedule.service_schedule.entity.vo.workForm.ShiftVo;
import com.schedule.service_schedule.mapper.WorkFormMapper;
import com.schedule.service_schedule.admin.service.WorkFormService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.schedule.service_schedule.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tpt
 * @since 2023-02-17
 */
@Slf4j
@Service
public class WorkFormServiceImpl extends ServiceImpl<WorkFormMapper, WorkForm> implements WorkFormService {
    @Resource
    private EmployeeClient employeeClient;

    @Resource
    private RuleService ruleService;

    //通过客流量数据生成班次并插入到数据库
    @Transactional
    @Override
    public void insertWorkForm(List<List<PassengerFlowExcelDto>> passengerFlowExcelVoLists, Map<String,String> ruleMap, double storeArea, String storeId) {
        //通过客流量数据生成客流量人数
        List<List<ShiftDto>> shiftsPeopleNum = ScheduleUtil.getShiftsPeopleNum(passengerFlowExcelVoLists, ruleMap, storeArea);
        shiftsPeopleNum.forEach(s->{s.forEach(l->{log.info(l.toString());});});
        List<List<WorkFormDto>> workForms = ScheduleUtil.GenerateShifts(shiftsPeopleNum, ruleMap);
        workForms.forEach(s->{
            s.forEach(w->{log.info(w.toString());});
        });
        //将班次信息插入到数据库
        try {
            for (List<WorkFormDto> list : workForms) {
                for (WorkFormDto workFormDto : list) {
                    WorkForm workForm=new WorkForm();
                    BeanUtils.copyProperties(workFormDto,workForm);
                    workForm.setStoreId(storeId);
                    workForm.setEmployeeId("");
                    workForm.setStartTime(TimeUtil.stringTimeToLocalTime(workFormDto.getStartTime()));
                    workForm.setEndTime(TimeUtil.stringTimeToLocalTime(workFormDto.getEndTime()));
                    workForm.setDate(LocalDate.parse(DateUtil.dealDateFormat(workFormDto.getDate())));
                    baseMapper.insert(workForm);
                }
            }
        }catch (Exception e){
            throw new ScheduleException(20001,"插入班次出错："+e.getMessage());
        }
    }

    /**
     * 获取班次通过门店id和时间范围
     * @param storeId
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public List<List<List<WorkForm>>> getWorkFormByStoreId(String storeId, LocalDate startDate, LocalDate endDate) {
        List<List<List<WorkForm>>> lists=new ArrayList<>();
        List<List<WorkForm>> listList=new ArrayList<>();
        List<WorkForm> workFormList=new ArrayList<>();
        LocalDate flagDate=null;
        LocalTime flagStartTime=null;
        LocalTime flagEndTime=null;
        QueryWrapper<WorkForm> wrapper=new QueryWrapper<>();
        wrapper.between("date",startDate,endDate);
        wrapper.orderByAsc("date","start_time","end_time");
        List<WorkForm> list=baseMapper.selectList(wrapper);
        log.info(list.size()+"");
        for(WorkForm workForm:list){
            workForm.setEmployeeId("");
            if(flagDate==null){
                flagDate=workForm.getDate();
            }else if(flagDate.compareTo(workForm.getDate())!=0){
                //日期不相同
                flagDate=workForm.getDate();
                listList.add(workFormList);
                lists.add(listList);
                listList=new ArrayList<>();
            }
            if(flagStartTime==null){
                flagStartTime=workForm.getStartTime();
                flagEndTime=workForm.getEndTime();
                workFormList.add(workForm);
            }else if(flagEndTime.compareTo(workForm.getEndTime())==0&&flagStartTime.compareTo(workForm.getStartTime())==0){
                workFormList.add(workForm);
            }else {
                flagEndTime=workForm.getEndTime();
                flagStartTime=workForm.getStartTime();
                listList.add(workFormList);
                workFormList=new ArrayList<>();
                workFormList.add(workForm);
            }
        }
        listList.add(workFormList);
        lists.add(listList);
        return lists;
    }

    /**
     * 获取一天的班次
     * @param storeId
     * @param date
     * @return
     */
    @Override
    public List<WorkForm> getDayShift(String storeId, LocalDate date) {
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("store_id",storeId);
        wrapper.eq("date",date);
        wrapper.orderByAsc("start_time","end_time");
        List<WorkForm> workFormList=baseMapper.selectList(wrapper);
        List<User> userList=employeeClient.getAllEmployeeMessage(storeId);
        for (WorkForm workForm:workFormList){
            for(User user:userList){
                if(user.getId().equals(workForm.getEmployeeId())){
                    workForm.setUser(user);
                    break;
                }
            }
        }
        return workFormList;
    }

    /**
     * 获取周班次
     * @param storeId
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public List<List<WorkForm>> getWeekShift(String storeId, LocalDate startDate, LocalDate endDate,String position,String employeeName) {
        List<List<WorkForm>> lists=new ArrayList<>();
        List<WorkForm> list=null;
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("store_id",storeId);
        wrapper.between("date",startDate,endDate);
        wrapper.orderByAsc("date","start_time","end_time");

        List<User> userList=employeeClient.getAllEmployeeMessage(storeId);
        String employeeId="";
        if(position!=null&&position!=""){
            String []s=position.split(",");
            for (int i=0;i<s.length;i++){
                String s1=s[i];
                wrapper.like("allow_career",s1);
                wrapper.or();
            }
            wrapper.like("allow_career","all");
        }

        if(employeeName!=null&&employeeName!=""){
            for(User user:userList){
                if(user.getName().equals(employeeName)){
                    employeeId=user.getId();
                    break;
                }
            }
            wrapper.eq("employee_id",employeeId);
        }
        List<WorkForm> resultList=baseMapper.selectList(wrapper);

        LocalDate flagDate=null;
        for(WorkForm workForm:resultList){
            if(workForm.getEmployeeId()!=null&&workForm.getEmployeeId()!=""){
                for(User user:userList){
                    if(user.getId().equals(workForm.getEmployeeId())){
                        workForm.setUser(user);
                        break;
                    }
                }
            }

            if(flagDate==null){
                flagDate=workForm.getDate();
                list=new ArrayList<>();
                list.add(workForm);
            }else if(flagDate.compareTo(workForm.getDate())==0){
                list.add(workForm);
            }else{
                flagDate=workForm.getDate();
                lists.add(list);
                list=new ArrayList<>();
            }
        }
        //将最后的放进去
        lists.add(list);
        return lists;
    }
    //给班次排员工
    @Override
    public List<List<List<WorkForm>>> scheduleShifts(String storeId, LocalDate startDate, LocalDate endDate) {
        //获取需要的规则值
        Map<String,String> ruleMap=new HashMap<>();
        QueryWrapper<Rule> wrapper=new QueryWrapper<>();
        List<String> list=new ArrayList<>();
        list.add("午餐时间规则");
        list.add("工作时长规则");
        list.add("晚餐时间规则");
        list.add("休息时间规则");
        wrapper.in("type",list);
        List<Rule> ruleList = ruleService.list(wrapper);
        for(Rule rule:ruleList){
            ruleMap.put(rule.getType(),rule.getValue());
        }
        //获取门店员工
        List<User> userList= employeeClient.getAllEmployeeMessage(storeId);
        for(User user:userList){

            Preference preference= employeeClient.getPreference(user.getId());
            user.setPreference(preference);
        }
        List<EmployeeDto> employeeDtoList=EmployeeDtoUtil.dealEmployee(userList,ruleMap.get("工作时长规则"));
        //获取班次
        List<List<List<WorkForm>>> workForms=getWorkFormByStoreId(storeId,startDate,endDate);
        List<List<List<WorkFormDto>>> shifts= WorkFormDtoUtil.dealWorkFormToWorkFormDto(workForms);
        ScheduleUtil.scheduling(shifts,employeeDtoList,ruleMap);
        return WorkFormDtoUtil.dealWorkFormDtoToWorkForm(shifts);
    }

    //更新排完班次后的班次员工
    @Override
    @Transactional
    public void updateById(List<List<List<WorkForm>>> shifts) {
        for(List<List<WorkForm>> lists:shifts){
            for(List<WorkForm> workFormList:lists){
                for(WorkForm workForm:workFormList){
                    baseMapper.updateById(workForm);
                }
            }
        }
    }

    //获取某个班次的符合员工
    @Override
    public List<User> getConformEmployee(ShiftVo shiftVo) {
        //获取时间段里面的班次
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("date",shiftVo.getDate());
        wrapper.gt("start_time",shiftVo.getStartTime());
        wrapper.lt("start_time",shiftVo.getEndTime());
        List<WorkForm> workFormList=baseMapper.selectList(wrapper);
        wrapper=new QueryWrapper();
        wrapper.eq("date",shiftVo.getDate());
        wrapper.gt("end_time",shiftVo.getStartTime());
        wrapper.lt("end_time",shiftVo.getEndTime());
        workFormList.addAll(baseMapper.selectList(wrapper));
        //获取所有的员工
        List<User> userList=employeeClient.getAllEmployeeMessage(shiftVo.getStoreId());
        //统计班次里面的员工id
        List<String> employeeIdList=new ArrayList<>();
        for(WorkForm workForm:workFormList){
            if(!employeeIdList.contains(workForm.getEmployeeId())){
                employeeIdList.add(workForm.getEmployeeId());
            }
        }
        //匹配员工id，删除没有出现的员工
        for(int i=0;i<userList.size();i++){
            User user=userList.get(i);
            boolean flag=false;
            for(String employeeId:employeeIdList){
                if(user.getId().equals(employeeId)){
                    flag=true;
                    break;
                }
            }
            if(flag){
                userList.remove(user);
                i--;
            }
        }
        //获取员工的班次计算周时间和当天的时间
        for(User user:userList){
            wrapper=new QueryWrapper();
            wrapper.eq("employee_id",user.getId());
            wrapper.between("date",shiftVo.getStartDate(),shiftVo.getEndDate());
            wrapper.eq("store_id",shiftVo.getStoreId());
            List<WorkForm> list=baseMapper.selectList(wrapper);
            for(WorkForm workForm:list){
                double shiftTime=TimeUtil.calculateStringTimeSub(workForm.getStartTime().toString(),workForm.getEndTime().toString());
                if(workForm.getDate().compareTo(shiftVo.getDate())==0){
                    user.setDayWorkTime(user.getDayWorkTime()+shiftTime);
                }else {
                    user.setWeekWorkTime(user.getWeekWorkTime()+shiftTime);
                }
            }
        }
        //排序
        userList.sort(new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                int flag=(int) (o1.getWeekWorkTime()-o2.getWeekWorkTime());
                if(flag==0){
                    flag=(int)(o1.getDayWorkTime()-o2.getDayWorkTime());
                }
                return flag;
            }
        });
        return userList;
    }

    //获取生成班次excel表的数据
    @Override
    public List<List<ShiftExcelVo>> getShiftExcelInfo(String storeId, LocalDate startDate,LocalDate endDate) {
        List<List<ShiftExcelVo>> lists=new ArrayList<>();
        List<ShiftExcelVo> shiftExcelVos=null;
        QueryWrapper<WorkForm> wrapper=new QueryWrapper<>();
        wrapper.eq("store_id",storeId);
        wrapper.between("date",startDate,endDate);
        List<WorkForm> workForms=baseMapper.selectList(wrapper);
        List<User> userList=employeeClient.getAllEmployeeMessage(storeId);
        LocalDate flag=null;
        for(WorkForm workForm:workForms){
            ShiftExcelVo shiftExcelVo=new ShiftExcelVo();
            BeanUtils.copyProperties(workForm,shiftExcelVo);
            shiftExcelVo.setDate(workForm.getDate().toString());
            shiftExcelVo.setStartTime(workForm.getStartTime().toString());
            shiftExcelVo.setEndTime(workForm.getEndTime().toString());
            if(flag==null){
                flag=workForm.getDate();
                shiftExcelVos=new ArrayList<>();
                shiftExcelVos.add(shiftExcelVo);
            }else if(flag.compareTo(workForm.getDate())==0){
                shiftExcelVos.add(shiftExcelVo);
            }else{
                lists.add(shiftExcelVos);
                flag=workForm.getDate();
                shiftExcelVos=new ArrayList<>();
                shiftExcelVos.add(shiftExcelVo);
            }
            for(User user:userList){
                if (workForm.getEmployeeId().equals(user.getId())) {
                    shiftExcelVo.setEmployeeName(user.getName());
                    shiftExcelVo.setPhone(user.getPhone());
                    break;
                }
            }
        }
        lists.add(shiftExcelVos);
        return lists;
    }

    //获取月班次
    @Override
    public List<List<WorkForm>> getMonthShifts(String storeId, LocalDate startDate, LocalDate endDate) {
        List<List<WorkForm>> lists=new ArrayList<>();
        List<WorkForm> list=null;
        QueryWrapper<WorkForm> wrapper=new QueryWrapper<>();
        wrapper.eq("store_id",storeId);
        wrapper.between("date",startDate,endDate);
        wrapper.orderByAsc("date","start_time","end_time");
        List<WorkForm> queryList=baseMapper.selectList(wrapper);
        List<User> userList=employeeClient.getAllEmployeeMessage(storeId);
        LocalDate flag=null;
        for(WorkForm workForm:queryList){
            for(User user:userList){
                if(user.getId().equals(workForm.getEmployeeId())){
                    workForm.setUser(user);
                    break;
                }
            }
            if(flag==null){
                flag=workForm.getDate();
                list=new ArrayList<>();
                list.add(workForm);
            }else if(flag.compareTo(workForm.getDate())==0){
                list.add(workForm);
            }else {
                flag=workForm.getDate();
                lists.add(list);
                list=new ArrayList<>();
                list.add(workForm);
            }
        }
        lists.add(list);
        return lists;
    }
}
