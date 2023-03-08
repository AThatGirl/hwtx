package com.cj.personal.controller;


import com.cj.common.vo.ResultVO;
import com.cj.personal.service.UploadService;
import com.cj.personal.vo.AvatarUploadVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传
 *
 * @author 杰瑞
 * @date 2023/03/07
 */
@RestController
@CrossOrigin
@Api(tags = "上传文件")
public class UploadController {

    @Autowired
    private UploadService uploadService;


    @PostMapping("/avatarUpload")
    @ApiOperation("上传头像")
    public ResultVO avatarUpload(@RequestParam("file") MultipartFile file, @RequestParam("id") String id) {
        return uploadService.avatarUpload(id, file);
    }



}
