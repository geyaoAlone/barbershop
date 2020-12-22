package com.geyao.barbershop.common;

import com.alibaba.fastjson.JSON;
import com.geyao.barbershop.constants.BuziConstant;
import com.geyao.barbershop.dao.RedisDao;
import com.geyao.barbershop.deal.pojo.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class CommonService {


    @Resource
    public MongoTemplate template;
    @Resource
    public RedisDao redis;

    public String queryConfigValue(String code){
        List<Config> list = null;
        try {
            list = (List<Config>) redis.get(BuziConstant.CONFIG_TABLE_NAME);
            log.info("redis query config data：{}", JSON.toJSONString(list));
        }catch (Exception e){
            e.printStackTrace();
        }
        if(list == null || list.isEmpty()){
            list = template.findAll(Config.class,BuziConstant.CONFIG_TABLE_NAME);
            log.info("mongo query config data：{}", JSON.toJSONString(list));
        }
        Optional<Config> config = list.stream().filter(c -> code.equals(c.getCode())).findFirst();
        return config.get().getValue();
    }

}
