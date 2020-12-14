package com.geyao.barbershop.deal.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geyao.barbershop.constants.BuziConstant;
import com.geyao.barbershop.deal.pojo.BuyProduct;
import com.geyao.barbershop.deal.pojo.TransRecord;
import com.geyao.barbershop.user.pojo.User;
import com.geyao.barbershop.utils.CommonUtils;
import com.geyao.barbershop.utils.DateUtils;
import com.mongodb.client.result.UpdateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;

@Service
public class DealService {
    private static final Logger LOG = LoggerFactory.getLogger(DealService.class);


    @Resource
    MongoTemplate template;

    public boolean  saveBuyProduct(BuyProduct buyProduct){
        buyProduct.setId(CommonUtils.getMajorKeyId(BuziConstant.PRODUCT_ID_START));
        buyProduct.setStatus(BuziConstant.SUCCESS_CODE);
        buyProduct.setCreateTime(new Date());
       return template.save(buyProduct,BuziConstant.PRODUCT_TABLE_NAME) != null;
    }

    public boolean saveTransRecord(TransRecord transRecord){
        Date saveDate = new Date();
        LOG.info("trans record save time :{}", DateUtils.dateToStr(saveDate));
        transRecord.setId(CommonUtils.getMajorKeyId(BuziConstant.TRANS_ID_START));
        transRecord.setStatus(BuziConstant.SUCCESS_CODE);
        transRecord.setCreateTime(saveDate);
        return template.save(transRecord,BuziConstant.TRANS_TABLE_NAME) != null;
    }

    /**
     * 废除记录
     * @param id
     * @param collectionName
     * @return
     */
    public boolean discard(String id,String collectionName){
        Query query = new Query(Criteria.where("id").is(id));
        UpdateDefinition t = Update.update("status",BuziConstant.FAIL_CODE);
        UpdateResult res= template.updateFirst(query,t,BuyProduct.class,collectionName);
        System.out.println(JSON.toJSONString(res));
        return 1 == res.getMatchedCount();
    }

    /**
     * 删除记录
     * @param id
     * @param collectionName
     * @return
     */
    public boolean delete(String id,String collectionName){
        Query query = new Query(Criteria.where("id").is(id));
        return 1 == template.remove(query,collectionName).getDeletedCount();
    }

    /**查询**/
    public BuyProduct queryBuyProduct(String name){
        Query query = new Query();
        query.addCriteria(Criteria.where("productName").is(name));
        return template.findOne(query,BuyProduct.class,BuziConstant.PRODUCT_TABLE_NAME);
    }

    public List<BuyProduct> queryBuyProducts(int type){
        Query query = new Query();
        query.addCriteria(Criteria.where("status").is(BuziConstant.SUCCESS_CODE));
        //if(type != 0){
        //    query.addCriteria(Criteria.where("productType").is(type));
        //}
        return template.find(query,BuyProduct.class,BuziConstant.PRODUCT_TABLE_NAME);
    }

    /**
     * 查询交易记录
     * @param mobile
     * @param beforeDate 8位日期：查询这个日期之后的数据 不包含这个日期
     * @param type
     * @return
     */
    public List queryTransList(String mobile,Date beforeDate,int type){
        Query query = new Query();
        query.addCriteria(Criteria.where("status").is(BuziConstant.SUCCESS_CODE));
        if (!StringUtils.isEmpty(mobile)){
            query.addCriteria(Criteria.where("mobile").is(mobile));
        }
        //类型==0代表无类型
        if(type != 0){
            query.addCriteria(Criteria.where("transType").is(type));
        }
        if(!StringUtils.isEmpty(beforeDate)) {
            query.addCriteria(Criteria.where("createTime").gt(beforeDate));
        }
        query.with(Sort.by(Sort.Order.desc("id")));
        return template.find(query,TransRecord.class);
    }


    /*****统计类*****/
    public int countUser(){
        Query query = new Query(Criteria.where("role").is(BuziConstant.CUSTOMER_ROLE_CODE));
        return template.find(query, User.class,BuziConstant.USER_TABLE_NAME).size();
    }

    /**
     *
     * @param mobile
     * @param beforeDate 8位日期：查询这个日期之后的数据 不包含这个日期
     * @return
     */
    public List<JSONObject> countTrans(String mobile,Date beforeDate){
        Criteria criteria = Criteria.where("status").is(BuziConstant.SUCCESS_CODE);
        if(!StringUtils.isEmpty(mobile)){
            criteria.andOperator(Criteria.where("mobile").is(mobile));
        }
        if(!StringUtils.isEmpty(beforeDate)){
            criteria.andOperator(Criteria.where("createTime").gt(beforeDate));
        }
        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                Aggregation.group("transType").sum("amount").as(BuziConstant.TOTAL_AMOUNT).count().as(BuziConstant.TOTAL_ITEMS)
        );

        AggregationResults<JSONObject> ar = template.aggregate(aggregation, BuziConstant.TRANS_TABLE_NAME, JSONObject.class);
        return ar.getMappedResults();
    }





}
