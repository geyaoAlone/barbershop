package com.geyao.barbershop.deal.web;

import com.alibaba.fastjson.JSONObject;
import com.geyao.barbershop.common.ResultVo;
import com.geyao.barbershop.deal.pojo.BuyProduct;
import com.geyao.barbershop.deal.pojo.TransRecord;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/deal")
public interface DealController {

    @GetMapping("/firstPageData")
    public ResultVo firstPageData();

    @GetMapping("/transPageData")
    public ResultVo transPageData(int type);

    @GetMapping("/getTodayRecord")
    public ResultVo getTodayRecord();

    /**
     * 查交易记录
     * @param mobile
     * @param type
     * @return
     */
    @GetMapping("/getTransRecords")
    public ResultVo getTransRecords(String mobile,int type);

    @GetMapping("/product")
    public ResultVo queryProduct(int type);


    @PostMapping("/addProduct")
    public ResultVo addProduct(@RequestBody BuyProduct obj);

    @PostMapping("/addTrans")
    public ResultVo saveTransRecord(@RequestBody TransRecord record);

    @GetMapping("/delProduct")
    public ResultVo delProduct(String id);

    @GetMapping("/delRecord/{type}/{id}")
    public ResultVo delRecord(int type,String id);


    @GetMapping("/userInfo")
    public ResultVo queryUserInfo(String cMobile);

    @GetMapping("/transList")
    public ResultVo transList(String cMobile);
}

