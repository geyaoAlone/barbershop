package com.geyao.barbershop.user.pojo;

import lombok.Data;

@Data
public class Statistics{
    //规则：key_code
    private String totalItems_1;//累计充值条数
    private String totalItems_2;//累计消费条数
    private String totalAmount_1;//充值总金额
    private String totalAmount_2;//消费总金额

    public Statistics(){

    }

    public Statistics(String totalItems_1, String totalItems_2, String totalAmount_1, String totalAmount_2) {
        this.totalItems_1 = totalItems_1;
        this.totalItems_2 = totalItems_2;
        this.totalAmount_1 = totalAmount_1;
        this.totalAmount_2 = totalAmount_2;
    }
}
