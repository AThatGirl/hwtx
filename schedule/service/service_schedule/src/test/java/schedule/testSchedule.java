package schedule;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.schedule.service_schedule.ScheduleApplication;
import com.schedule.service_schedule.entity.Rule;
import com.schedule.service_schedule.entity.WorkForm;
import com.schedule.service_schedule.entity.dto.EmployeeDto;
import com.schedule.service_schedule.entity.dto.WorkFormDto;
import com.schedule.service_schedule.admin.service.RuleService;
import com.schedule.service_schedule.admin.service.WorkFormService;
import com.schedule.service_schedule.utils.EmployeeDtoUtil;
import com.schedule.service_schedule.utils.ScheduleUtil;
import com.schedule.service_schedule.utils.WorkFormDtoUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScheduleApplication.class)
public class testSchedule {
    @Resource
    private WorkFormService workFormService;
    @Resource
    private RuleService ruleService;
    @Test
    public void test1(){
        List<List<List<WorkForm>>> lists=workFormService.getWorkFormByStoreId("1", LocalDate.parse("2023-05-08"),LocalDate.parse("2023-05-14"));
        List<EmployeeDto> employeeDtoList=new ArrayList<>();
        //{"workTime":"all","weekWorkTime":"0","dayWorkTime":"0"}
        EmployeeDto employeeDto=new EmployeeDto();
        employeeDto.setId("1");
        employeeDto.setPreference("{\"workTime\":\"all\",\"weekWorkTime\":\"0\",\"dayWorkTime\":\"0\"}");
        employeeDtoList.add(employeeDto);
        employeeDto=new EmployeeDto();
        employeeDto.setId("2");
        employeeDto.setPreference("{\"workTime\":\"all\",\"weekWorkTime\":\"0\",\"dayWorkTime\":\"0\"}");
        employeeDtoList.add(employeeDto);
        employeeDto=new EmployeeDto();
        employeeDto.setId("3");
        employeeDto.setPreference("{\"workTime\":\"all\",\"weekWorkTime\":\"0\",\"dayWorkTime\":\"0\"}");
        employeeDtoList.add(employeeDto);
        employeeDto=new EmployeeDto();
        employeeDto.setId("4");
        employeeDto.setPreference("{\"workTime\":\"Mon&08:00-18:00;Tue-Sat&19:00-22:00\",\"weekWorkTime\":\"8\",\"dayWorkTime\":\"35\"}");
        employeeDtoList.add(employeeDto);

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
        EmployeeDtoUtil.dealEmployeeDto(employeeDtoList,ruleMap.get("工作时长规则"));
        List<List<List<WorkFormDto>>> scheduleList=WorkFormDtoUtil.dealWorkFormToWorkFormDto(lists);
        ScheduleUtil.scheduling(scheduleList,employeeDtoList,ruleMap);
        scheduleList.forEach(l->{
            l.forEach(s->{
                s.forEach(a->{
                    if(a.getEmployeeId()!=null&&a.getEmployeeId()!=""){
                        log.info(a.toString());
                    }
                });
            });
        });
    }
    @Test
    public void test2(){
        LocalDate localDate=LocalDate.parse("2023-05-08");
        LocalDate localDate1=LocalDate.parse("2023-05-08");
        System.out.println(localDate.compareTo(localDate1));
    }
}
