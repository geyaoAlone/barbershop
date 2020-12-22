package com.geyao.barbershop.deal.web.impl;

import com.alibaba.fastjson.JSONObject;
import com.geyao.barbershop.common.BaseController;
import com.geyao.barbershop.common.ResultVo;
import com.geyao.barbershop.constants.BuziConstant;
import com.geyao.barbershop.deal.pojo.BuyProduct;
import com.geyao.barbershop.deal.pojo.TransRecord;
import com.geyao.barbershop.deal.web.DealController;
import com.geyao.barbershop.user.pojo.User;
import com.geyao.barbershop.utils.AmountUtil;
import com.geyao.barbershop.utils.DateUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class DealControllerImpl extends BaseController implements DealController {

    @Override
    public ResultVo queryNormalUser() {
        String mobile = getMobile();
        if(StringUtils.isEmpty(mobile)){
            return new ResultVo("token异常！");
        }
        List<User> u = user.queryNormalUser(mobile);
        if(Objects.isNull(u)){
            return new ResultVo("用户异常！");
        }
        return new ResultVo(u.get(0));
    }
    /**
     *  首页查询
     * @return
     */
    @Override
    public ResultVo firstPageData(){
        String mobile = getMobile();
        if(StringUtils.isEmpty(mobile)){
            return new ResultVo("token异常！");
        }
        User u = user.queryUser(mobile);
        if(Objects.isNull(u)){
            return new ResultVo("用户异常！");
        }
        JSONObject data = new JSONObject();
        data.put("user",u);
        if(BuziConstant.CUSTOMER_ROLE_CODE == u.getRole()){
            List<TransRecord> trans = service.queryTransList(u.getMobile(),getBeforeDate(Integer.parseInt(user.queryConfigValue(BuziConstant.CUSTOMER_QUERY_DATE_CODE))),0 );
            trans.stream().forEach(record -> {
                record.setAmountFmt(AmountUtil.yuanFormat(Double.toString(record.getAmount())));
                record.setTimeFmt(DateUtils.dateToStr(record.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
            });
            data.put("trans",trans);
        }else{
            data.put("statistic",getCount(null,-1));
        }
        return new ResultVo(data);
    }


    @Override
    public ResultVo transPageData(int type) {
        String checkRes = checkAuth();
        if(!"".equals(checkRes)){
            return new ResultVo(checkRes);
        }
        JSONObject data = new JSONObject();
        List<User> uList = user.queryNormalUser(null);
        List<String> strList = new ArrayList<>();
        JSONObject userInfo = new JSONObject();
        uList.stream().forEach(us ->{
            strList.add(us.getMobile()+"/"+us.getUserId());
            userInfo.put(us.getMobile()+"/"+us.getUserId(),us);
        });
        data.put("strList",strList);
        data.put("userInfo",userInfo);

        List<BuyProduct> proList = service.queryBuyProducts(type);
        List<String> ptrList = new ArrayList<>();
        JSONObject productInfo = new JSONObject();
        proList.stream().forEach(b ->{
            ptrList.add(b.getProductName()+"（"+b.getProductAmt()+"元）");
            productInfo.put(b.getProductName()+"（"+b.getProductAmt()+"元）",b);
        });
        data.put("productList",ptrList);
        data.put("productInfo",productInfo);
        return new ResultVo(data);
    }

    /**
     *  店主查询今天交易
     * @return
     */
    @Override
    public ResultVo getTodayRecord(){
        String checkRes = checkAuth();
        if(!"".equals(checkRes)){
            return new ResultVo(checkRes);
        }
        JSONObject data = new JSONObject();
        List<TransRecord> trans = service.queryTransList(null,getBeforeDate(1),0);
        trans.stream().forEach(record -> {
            record.setAmountFmt(AmountUtil.yuanFormat(Double.toString(record.getAmount())));
            record.setTimeFmt(DateUtils.dateToStr(record.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
        });
        data.put("trans",trans);
        data.put("statistic",getCount(null,1));
        return new ResultVo(data);
    }

    @Override
    public ResultVo getTransRecords(String mobile, int type) {
        String checkRes = checkAuth();
        if(!"".equals(checkRes)){
            return new ResultVo(checkRes);
        }
        return new ResultVo(service.queryTransList(mobile,null,type));
    }




    @Override
    public ResultVo queryProduct(int type){
        String checkRes = checkAuth();
        if(!"".equals(checkRes)){
            return new ResultVo(checkRes);
        }
        List<BuyProduct> list = service.queryBuyProducts(type);
        if(list == null || list.isEmpty()){
            return new ResultVo(new ArrayList<BuyProduct>());
        }
        list.forEach(pro -> {
            pro.setProductAmt(AmountUtil.yuanFormat(pro.getProductAmt()));
            pro.setTimeFmt(DateUtils.dateToStr(pro.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
        });
        return new ResultVo(list);
    }

    @Override
    public ResultVo addProduct(@RequestBody BuyProduct obj){
        String checkRes = checkAuth();
        if(!"".equals(checkRes)){
            return new ResultVo(checkRes);
        }
        if(!Objects.isNull(service.queryBuyProduct(obj.getProductName()))){
            return new ResultVo("产品名已被使用！");
        }
        return new ResultVo(service.saveBuyProduct(obj));
    }

    @Override
    public ResultVo saveTransRecord(TransRecord record) {
        String checkRes = checkAuth();
        if(!"".equals(checkRes)){
            return new ResultVo(checkRes);
        }

        String updateRes = user.updateUserBalance(record.getMobile(),record.getAmount(),record.getTransType());
        if(!"".equals(updateRes)){
            return new ResultVo("录入失败！" + updateRes);
        }
        return new ResultVo(service.saveTransRecord(record));
    }


    @Override
    public ResultVo queryUserInfo(String cMobile){
        String checkRes = checkAuth();
        if(!"".equals(checkRes)){
            return new ResultVo(checkRes);
        }
        List<User> list = user.queryNormalUser(cMobile);
        list.stream().forEach(user -> {
            user.setStatistic(getCount(user.getMobile(),-1));
            user.setBalanceFmt(AmountUtil.yuanFormat(Double.toString(user.getBalance())));
            user.setTimeFmt(DateUtils.dateToStr(user.getLastDealTime(),"yyyy-MM-dd HH:mm:ss"));
        });
        return new ResultVo(list);
    }

    @Override
    public ResultVo transList(String cMobile) {
        String checkRes = checkAuth();
        if(!"".equals(checkRes)){
            return new ResultVo(checkRes);
        }
        if(StringUtils.isEmpty(cMobile)){
            return new ResultVo("查询明细参数异常");
        }
        List<TransRecord> list = service.queryTransList(cMobile,null,0);
        list.stream().forEach(record -> {
            record.setAmountFmt(AmountUtil.yuanFormat(Double.toString(record.getAmount())));
            record.setTimeFmt(DateUtils.dateToStr(record.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
        });
        return new ResultVo(list);
    }

    @Override
    public ResultVo delProduct(String id) {
        String checkRes = checkAuth();
        if(!"".equals(checkRes)){
            return new ResultVo(checkRes);
        }
        if(StringUtils.isEmpty(id)){
            return new ResultVo("删除失败！产品异常");
        }

        if (service.discard(id, BuziConstant.PRODUCT_TABLE_NAME)) {
            return new ResultVo(true);
        } else {
            return new ResultVo("删除失败");
        }
    }

    @Override
    public ResultVo delRecord(int type,String id){


        return new ResultVo(true);
    }

}
