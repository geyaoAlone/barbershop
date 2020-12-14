package com.geyao.barbershop.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.geyao.barbershop.aspect.HttpAspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class SmsUtils {
    private static final Logger LOG = LoggerFactory.getLogger(SmsUtils.class);

    public static boolean aliyunSendSms(String mobile,String code) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "", "");
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("SignName", "剪艺人生");
        request.putQueryParameter("TemplateCode", "SMS_206553397");//
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");//{"name":"123732"}
        try {
            CommonResponse response = client.getCommonResponse(request);
            if(response.getHttpStatus() != 200){
                LOG.info("aliyunSendSms error ！ return code {}",response.getHttpStatus());
                return false;
            }
            JSONObject resData = JSON.parseObject(response.getData());
            if(!"OK".equals(resData.getString("Code"))){
                LOG.info("aliyunSendSms fail ! return msg {}",response.getData());
                return false;
            }
        } catch (ServerException e) {
            e.printStackTrace();
            LOG.info("aliyunSendSms error ！ return ServerException {}",e.getMessage());
            return false;
        } catch (ClientException e) {
            e.printStackTrace();
            LOG.info("aliyunSendSms error ！ return ClientException {}",e.getMessage());
            return false;
        }

        return true;
    }


}
