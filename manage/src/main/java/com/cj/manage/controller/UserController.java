package com.cj.manage.controller;


import com.cj.common.entity.User;
import com.cj.common.vo.ResultVO;
import com.cj.manage.service.UserService;
import com.cj.manage.vo.UserSearchVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manage/user")
@Api(tags = "用户管理", value = "用户管理")
public class UserController {

    public static final Integer DEFAULT_PAGE_NUM = 1;

    @Autowired
    private UserService userService;

    @PostMapping("/search")
    @ApiOperation("条件查询")
    public ResultVO search(@RequestBody UserSearchVO userSearchVO){
        return userService.search(userSearchVO);
    }

    @PostMapping("/changeUserInfo")
    @ApiOperation("修改用户信息")
    public ResultVO changeUserInfo(@RequestBody User user){
        return userService.changeUserInfo(user);
    }


    @PostMapping("/deleteUser")
    @ApiOperation("删除用户")
    public ResultVO deleteUser(@RequestBody String[] ids){
        return userService.deleteUser(ids);
    }

    @PostMapping("/examine")
    @ApiOperation("审核用户")
    public ResultVO examine(@RequestParam("id") String id, @RequestParam("pass") Integer pass){
        return userService.examine(id, pass);
    }

}
