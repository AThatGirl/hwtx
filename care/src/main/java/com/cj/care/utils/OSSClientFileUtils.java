package com.cj.care.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Component
public class OSSClientFileUtils {

    //上传到的目录
    public static final String DIR = "avatars";

    @Value("${aliyun.oss.endpoint}")
    private String endpoint; // 地域节点

    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId; // AccessKeyId

    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecret; // AccessKeySecret

    @Value("${aliyun.oss.bucketName}")
    private String bucketName; // 存储桶名称

    private OSS ossClient;

    /**
     * 初始化 OSS 客户端
     */
    public void initClient() {
        ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

    /**
     * 上传文件到 OSS
     *
     * @param file 要上传的文件
     * @return 文件的 URL
     */
    public String uploadFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString() + extension;
        String key = DIR + "/" + filename;
        ossClient.putObject(bucketName, key, file.getInputStream());
        return "https://" + bucketName + "." + endpoint + "/" + key;
    }

    /**
     * 关闭 OSS 客户端
     */
    public void closeClient() {
        ossClient.shutdown();
    }
}
