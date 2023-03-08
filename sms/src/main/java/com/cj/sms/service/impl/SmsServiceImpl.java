package com.cj.sms.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

import com.cj.common.vo.ResultVO;
import com.cj.sms.service.SmsService;
import com.cj.sms.utils.CodeUtils;
import com.cj.sms.utils.RandomUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service
public class SmsServiceImpl implements SmsService {

    private static final String templateCode = "SMS_268560901";

    /**
     * 发送验证码
     *
     * @param PhoneNumbers 电话号码
     * @param param        参数
     * @return boolean
     */
    public boolean sendCode(String PhoneNumbers, Map<String, Object> param) {
        if (StringUtils.isEmpty(PhoneNumbers)) {
            return false;
        }
        DefaultProfile profile =
                DefaultProfile.getProfile("cn-qingdao", "LTAI5tPqmkuAZMN8cM5vzWLV", "mLO40ZQh87dH3LIJodnnJPsOCLsai9");
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("PhoneNumbers", PhoneNumbers); //手机号
        request.putQueryParameter("SignName", "陈杰的个人网站"); //签名名称
        request.putQueryParameter("TemplateCode", templateCode); //阿里云模板
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param)); //验证码数据转换json
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            return response.getHttpResponse().isSuccess();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 发送
     *
     * @param phone         电话
     * @param redisTemplate 复述,模板
     * @return {@link ResultVO}
     */
    @Override
    public ResultVO send(String phone, RedisTemplate<String, String> redisTemplate) {
        if (CodeUtils.checkCode(phone, redisTemplate)) {
            CodeUtils.deleteCode(phone, redisTemplate);
        }
        String code = RandomUtils.getFourBitRandom();
        Map<String, Object> param = new HashMap<>();
        param.put("code", code);
        boolean isSend = sendCode(phone, param);
        if (isSend) {
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
            return ResultVO.success().setMessage("发送短信成功");
        } else {
            return ResultVO.fail().setMessage("发送短信失败");
        }
    }

    @Override
    public ResultVO getNoteByPhone(String phone, RedisTemplate<String, String> redisTemplate) {
        //获取验证码
        String note = redisTemplate.opsForValue().get(phone);
        if (StringUtils.isEmpty(note)){
            return ResultVO.fail();
        }
        return ResultVO.success().setData(note);
    }

}