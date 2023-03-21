package com.cj.personal.controller;


import com.cj.common.entity.Preference;
import com.cj.common.vo.ResultVO;
import com.cj.personal.service.PreferService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "偏好设置")
@RequestMapping("/personal/preference")
public class PreferenceController {

    @Autowired
    private PreferService preferService;

    @GetMapping("/getPrefer/{id}")
    @ApiOperation("获取偏好信息")
    public ResultVO getPrefer(@PathVariable("id") String id){
        return preferService.getPrefer(id);
    }

    @PostMapping("/changePrefer")
    @ApiOperation("修改偏好")
    public ResultVO changePrefer(@RequestBody Preference preference){
        return preferService.changePrefer(preference);
    }




}
