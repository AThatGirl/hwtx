package com.cj.personal.controller;


import com.cj.common.vo.ResultVO;
import com.cj.personal.service.UploadService;
import com.cj.personal.utils.OSSClientUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 上传
 *
 * @author 杰瑞
 * @date 2023/03/07
 */
@RestController
@Api(tags = "上传文件")
@RequestMapping("/personal/upload")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @Autowired
    private OSSClientUtils ossClientUtils;

    @PostMapping("/avatarUpload")
    @ApiOperation("上传头像")
    public ResultVO avatarUpload(@RequestParam("file") MultipartFile file, @RequestParam("id") String id) {
        return uploadService.avatarUpload(id, file);
    }

    @PostMapping("/fileUpload")
    @ApiOperation("文件上传")
    public String fileUpload(@RequestParam("file") MultipartFile file){
        String uploadFile = "";
        try {
            ossClientUtils.initClient();
            uploadFile = ossClientUtils.uploadFile(file);
        } catch (IOException e) {
            e.printStackTrace();
            return uploadFile;
        } finally {
            ossClientUtils.closeClient();
        }
        return uploadFile;
    }


}
