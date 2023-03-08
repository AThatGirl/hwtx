package com.cj.personal.service;

import com.cj.common.vo.ResultVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传
 *
 * @author 杰瑞
 * @date 2023/03/07
 */
public interface UploadService {

    /**
     * 头像上传
     *
     * @param file 文件
     * @param id   id
     * @return {@link ResultVO}
     */
    ResultVO avatarUpload(String id, MultipartFile file);


}
