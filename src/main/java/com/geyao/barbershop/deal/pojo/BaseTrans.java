package com.geyao.barbershop.deal.pojo;

import lombok.Data;

import java.util.Date;
@Data
public class BaseTrans {


    private String id;       // 主键/编号/唯一
    private int status;      //状态
    private Date createTime; //创建时间
    private int transType;   //交易类型

    private String timeFmt;//格式化时间
    private String amountFmt;//格式化金额
}
