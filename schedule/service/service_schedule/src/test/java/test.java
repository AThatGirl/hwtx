import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.schedule.service_schedule.entity.WorkForm;
import com.schedule.service_schedule.entity.dto.EmployeePreferenceDto;
import com.schedule.service_schedule.entity.dto.RunDateTimeDto;
import com.schedule.service_schedule.entity.vo.client.Preference;
import com.schedule.service_schedule.entity.vo.client.User;
import com.schedule.service_schedule.entity.vo.excel.ShiftExcelVo;
import com.schedule.service_schedule.utils.EmployeeDtoUtil;
import com.schedule.service_schedule.utils.PreferenceUtil;
import com.schedule.service_schedule.utils.RuleUtil;
import com.schedule.service_schedule.utils.WeekUtil;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class test {
    @Test
    public void t(){
        List<EmployeePreferenceDto> list = PreferenceUtil.dealJSONStringToEmployeePreference("Sat-Sun&10:00-22:00;Mon-Fri&09:00-21:00");
        list.forEach(System.out::println);
    }

    @Test
    public void t1(){
        RunDateTimeDto runDateTimeDto=new RunDateTimeDto();
        runDateTimeDto.setEndTime("2023/05/10");
        String s= JSON.toJSONString(runDateTimeDto);
        System.out.println(s);
    }

    @Test
    public void t2(){
        Preference preference=new Preference();
        preference.setRangeTime("1,2,4,3");
        preference.setStartTime("10:00");
        preference.setEndTime("22:00");
        User user=new User();
        user.setId("123");
        user.setPreference(preference);
        user.setStoreId("1");
        List<User> list=new ArrayList<>();
        list.add(user);
        EmployeeDtoUtil.dealEmployee(list,"{\"weekWorkTime\":\"40\",\"dayWorkTime\":\"8\",\"shiftTimeRange\":\"2-4\",\"maxWorkTime\":\"4\"}");
    }

    @Test
    public void t3(){
        ShiftExcelVo shiftExcelVo=new ShiftExcelVo();
        WorkForm workForm=new WorkForm();
        workForm.setEndTime(LocalTime.now());
        BeanUtils.copyProperties( workForm,shiftExcelVo);
        System.out.println(shiftExcelVo);
    }

    @Test
    public void t4(){
        RuleUtil.getRunTimeValue("Mou-Fri&9:00-21:00;Sta-Sun&10:00-22:00").forEach(System.out::println);
    }
}
