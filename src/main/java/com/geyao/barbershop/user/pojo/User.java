package com.geyao.barbershop.user.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private String userId;//会员号
    private String mobile;//手机号
    private Date createTime; //创建时间

    private double balance;  //余额
    private Date lastDealTime;//最后一次交易时间
    private int status;//状态：0-废除。废除后余额回零

    private int role;//角色 ：1-普通人。99-店主。0-系统管理员

    private String validateCode;//验证码

    private Statistics statistic;//统计




    private String timeFmt;//格式化时间
    private String balanceFmt;  //格式化余额

}
