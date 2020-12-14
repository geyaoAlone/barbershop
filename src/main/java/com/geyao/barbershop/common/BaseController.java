package com.geyao.barbershop.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geyao.barbershop.constants.BuziConstant;
import com.geyao.barbershop.deal.service.DealService;
import com.geyao.barbershop.user.pojo.Statistics;
import com.geyao.barbershop.user.pojo.User;
import com.geyao.barbershop.user.service.UserService;
import com.geyao.barbershop.utils.AmountUtil;
import com.geyao.barbershop.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class BaseController {
    private static final Logger LOG = LoggerFactory.getLogger(BaseController.class);
    @Resource
    public DealService service;
    @Resource
    public UserService user;
    /**
     * d为负数是d天之前；正数是之后
     * @param d
     * @return
     */
    public static Date getBeforeDate(int d){
        Date date = null;
        try {
            Date beforedate = DateUtils.getDateBefore(new Date(),d);
            date = DateUtils.getStrToDate(DateUtils.dateToStr(beforedate,"yyyMMdd") + BuziConstant.MAX_SIX_TIME,"yyyyMMddHHmmss");
        }catch (ParseException e){
            e.printStackTrace();
        }
        return date;
    }

    public String getMobile(){
        try {
            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String checkAuth(){
        String mobile = getMobile();
        if(StringUtils.isEmpty(mobile)){
            return "权限异常!";
        }
        User u = user.queryUser(mobile);
        if(Objects.isNull(u) || BuziConstant.MASTER_ROLE_CODE != u.getRole()) {
            return "权限不够！";
        }
        return "";
    }

    /**
     * before < 0 视为空。无需使用时间查询
     * @param mobile
     * @param before
     * @return
     */
    public Statistics getCount(String mobile, int before){
        Date d = null;
        if(before >= 0){
            d = getBeforeDate(before);
        }
        List<JSONObject> cList = service.countTrans(mobile,d);
        if(cList == null || cList.isEmpty()){
            return new Statistics("0","0","0.00","0.00");
        }
        LOG.info("getCount query res data : {}", JSON.toJSONString(cList));
        Statistics s = new Statistics();
        cList.stream().forEach(json ->{
            if(BuziConstant.RECHARGE_TYPE_CODE == json.getIntValue(BuziConstant.TOTAL_ID)){
                s.setTotalItems_1(nullTo0(json.getString(BuziConstant.TOTAL_ITEMS)));
                s.setTotalAmount_1(AmountUtil.yuanFormat(nullTo0(json.getString(BuziConstant.TOTAL_AMOUNT))));
            }
            if(BuziConstant.CONSUME_TYPE_CODE == json.getIntValue(BuziConstant.TOTAL_ID)){
                s.setTotalItems_2(nullTo0(json.getString(BuziConstant.TOTAL_ITEMS)));
                s.setTotalAmount_2(AmountUtil.yuanFormat(nullTo0(json.getString(BuziConstant.TOTAL_AMOUNT))));
            }
        });
        if(s.getTotalItems_2() == null){
            s.setTotalItems_2("0");
        }
        if(s.getTotalAmount_2() == null){
            s.setTotalAmount_2("0.00");
        }
        if(s.getTotalItems_1() == null){
            s.setTotalItems_1("0");
        }
        if(s.getTotalAmount_1() == null){
            s.setTotalAmount_1("0.00");
        }
        return s;
    }

    private static String nullTo0(String str){
        if(str == null){
            return "0";
        }
        if(StringUtils.isEmpty(str)){
            return "0";
        }
        return str;
    }
}
