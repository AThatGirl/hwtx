package com.cj.manage.controller;


import com.cj.common.vo.ResultVO;
import com.cj.manage.service.UserService;
import com.cj.manage.vo.UserSearchVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manage/user")
@Api(tags = "用户管理", value = "用户管理")
public class UserController {

    public static final Integer DEFAULT_PAGE_NUM = 1;

    @Autowired
    private UserService userService;

    @GetMapping("/search")
    @ApiOperation("条件查询")
    private ResultVO search(@RequestParam String name, @RequestParam String status, @RequestParam String pageNum){
        UserSearchVO userSearchVO = new UserSearchVO();
        userSearchVO.setName(name);
        userSearchVO.setStatus(status);
        if (StringUtils.isEmpty(pageNum)){
            userSearchVO.setPageNum(DEFAULT_PAGE_NUM);
        }else {
            userSearchVO.setPageNum(Integer.valueOf(pageNum));
        }
        return userService.search(userSearchVO);
    }

    @PostMapping("/deleteUser")
    @ApiOperation("删除用户")
    private ResultVO deleteUser(@RequestBody String[] ids){
        return userService.deleteUser(ids);
    }

    @PostMapping("/examine")
    @ApiOperation("审核用户")
    private ResultVO examine(@RequestParam("id") String id, @RequestParam("pass") Integer pass){
        return userService.examine(id, pass);
    }

}
