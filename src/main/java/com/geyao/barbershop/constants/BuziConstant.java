package com.geyao.barbershop.constants;

public class BuziConstant {
    public static final String USER_VIP_START = "VIP";
    //客户角色码
    public static final int CUSTOMER_ROLE_CODE = 1;
    //店主角色码
    public static final int MASTER_ROLE_CODE = 99;

    //成功码
    public static final int SUCCESS_CODE = 1;
    //失败码
    public static final int FAIL_CODE = 0;

    //充值交易码
    public static final int RECHARGE_TYPE_CODE = 1;
    //消费交易吗
    public static final int CONSUME_TYPE_CODE = 2;
    //产品编号开头字母
    public static final String PRODUCT_ID_START = "Pr";
    //交易编号开头字母
    public static final String TRANS_ID_START = "Tr";

    /****************表名******************/
    public static final String CONFIG_TABLE_NAME = "config";
    public static final String PRODUCT_TABLE_NAME = "buyProduct";
    public static final String TRANS_TABLE_NAME = "transRecord";
    public static final String USER_TABLE_NAME = "user";
    /**********************************/

    public static final String MAX_SIX_TIME = "235959";
    //总条数；总金额key
    public static final String TOTAL_ITEMS = "totalItems";
    public static final String TOTAL_AMOUNT = "totalAmount";
    public static final String TOTAL_ID = "_id";


    /****************config_code****************/
    public static final String CONFIG_ADMIN_CODE = "adminMobile";
    public static final String CUSTOMER_QUERY_DATE_CODE = "customerQueryDate";
    public static final String ALIYUN_KEY_ID_CODE = "aliyunKeyId";
    public static final String ALIYUN_SECRET_CODE = "aliyunSecret";
    /*******************************************/


}
