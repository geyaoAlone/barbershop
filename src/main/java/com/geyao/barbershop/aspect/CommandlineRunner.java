package com.geyao.barbershop.aspect;

import com.alibaba.fastjson.JSON;
import com.geyao.barbershop.constants.BuziConstant;
import com.geyao.barbershop.dao.RedisDao;
import com.geyao.barbershop.deal.pojo.BuyProduct;
import com.geyao.barbershop.deal.pojo.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
@Slf4j
@Component
public class CommandlineRunner implements CommandLineRunner {

    @Resource
    MongoTemplate template;
    @Resource
    RedisDao redis;
    @Override
    public void run(String... args) throws Exception {
        log.info("start query config...");
        List<Config> list = template.findAll(Config.class,BuziConstant.CONFIG_TABLE_NAME);
        if(list == null || list.isEmpty()) {
            log.info("config is null!");
        }else {
            boolean setRes = redis.set(BuziConstant.CONFIG_TABLE_NAME, list);
            if (!setRes) {
                log.info("redis cache config is failed !");
            } else {
                log.info("redis cache success!");
            }
        }
    }
}
