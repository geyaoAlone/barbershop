package com.geyao.barbershop.user.service;

import com.geyao.barbershop.common.ResultVo;
import com.geyao.barbershop.constants.BuziConstant;
import com.geyao.barbershop.dao.RedisDao;
import com.geyao.barbershop.user.pojo.User;
import com.geyao.barbershop.utils.AmountUtil;
import com.geyao.barbershop.utils.SmsUtils;
import com.mongodb.client.result.UpdateResult;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private static final String[] TEST_MOBILE = {"18287181006"};
    @Resource
    MongoTemplate template;
    @Resource
    RedisDao redis;

    public boolean sendValidateCode(String mobile){
        if(ArrayUtils.contains(TEST_MOBILE,mobile)){
            return true;
        }
        String verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);//生成短信验证码
        if(redis.set(mobile + "_validate_code",verifyCode,320)
            && SmsUtils.aliyunSendSms(mobile,verifyCode)
            ){
            LOG.info("[{}] get validate code[{}] success!!!",mobile,verifyCode);
            return true;
        } else {
            return false;
        }
    }

    public String checkValidateCode(String mobile,String verifyCode){
        if(ArrayUtils.contains(TEST_MOBILE,mobile)){
            return "";
        }
        if(!redis.hasKey(mobile + "_validate_code")){
            return "验证码已过期";
        }
        String validateCode = (String)redis.get(mobile + "_validate_code");
        if(verifyCode.equals(validateCode)){
            return "";
        }else{
            return "验证码错误";
        }
    }

    public List<User> queryNormalUser(String mobile){
        Query query = new Query(Criteria.where("role").is(BuziConstant.CUSTOMER_ROLE_CODE));
        if(!StringUtils.isEmpty(mobile)){
            query.addCriteria(Criteria.where("mobile").is(mobile));
        }
        return template.find(query,User.class,BuziConstant.USER_TABLE_NAME);
    }


    public User queryUser(String mobile){
        Query query = new Query(Criteria.where("mobile").is(mobile));
        return template.findOne(query,User.class);
    }

    public synchronized String updateUserBalance(String mobile,double amont,int type){
        Query query = new Query(Criteria.where("mobile").is(mobile));
        double balance = template.findOne(query,User.class).getBalance();
        if(StringUtils.isEmpty(balance)){
            balance = 0.00;
        }
        //充值
        if(BuziConstant.RECHARGE_TYPE_CODE == type){
            balance = (new BigDecimal(balance).add(new BigDecimal(amont))).doubleValue();
        } else if(BuziConstant.CONSUME_TYPE_CODE == type){//消费
            if(AmountUtil.gt(amont,balance)){
                return "余额不足！";
            }
            balance = (new BigDecimal(balance).subtract(new BigDecimal(amont))).doubleValue();
        }else{
            return "交易类型错误！";
        }

        Update update = new Update();
        update.set("balance",balance);
        update.set("lastDealTime",new Date());
        UpdateResult result = template.upsert(query,update,User.class,BuziConstant.USER_TABLE_NAME);
        return result.getModifiedCount() == 1 ?"":"更新余额失败！";

    }
    public synchronized String getMaxUserId(){
        Query query = new Query(Criteria.where("role").is(BuziConstant.CUSTOMER_ROLE_CODE));
        query.with(Sort.by(Sort.Order.desc("userId")));
        List<User> users = template.find(query,User.class,BuziConstant.USER_TABLE_NAME);
        if(users == null || users.isEmpty()){
            return BuziConstant.USER_VIP_START + "0001";
        }
        String maxUserId = users.get(0).getUserId();
        maxUserId = maxUserId.substring(3,maxUserId.length());
        String b = String.valueOf(Integer.parseInt(maxUserId) + 1);
        switch (b.length()){
            case 1 : b = BuziConstant.USER_VIP_START + "000" + b; break;
            case 2 : b = BuziConstant.USER_VIP_START + "00"  + b; break;
            case 3 : b = BuziConstant.USER_VIP_START + "0"   + b; break;
            default: b = BuziConstant.USER_VIP_START + b;
        }
        return b;
    }



    public boolean saveUser(User user){
        user.setUserId(getMaxUserId());
        user.setCreateTime(new Date());
        user.setBalance(0.00);
        user.setStatus(BuziConstant.SUCCESS_CODE);
        user.setRole(BuziConstant.CUSTOMER_ROLE_CODE);
        user.setLastDealTime(new Date());
        return template.save(user) != null;
    }


}
