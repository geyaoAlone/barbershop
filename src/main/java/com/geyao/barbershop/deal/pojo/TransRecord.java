package com.geyao.barbershop.deal.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 交易记录
 */
@Data
public class TransRecord extends BaseTrans{

    private String userId;//客户号
    private String mobile;//手机号
    private String productId;//产品编号
    private String productName;//产品名
    private double amount;//金额

}
