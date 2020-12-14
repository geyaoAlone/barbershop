package com.geyao.barbershop.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 金额相关工具类
 */
public class AmountUtil {

    /**
     * 金额 元转分
     * @param yuanAmount
     * @return
     */

    public static long yanToFen(String yuanAmount){
        return yuanToFen(new BigDecimal(yuanAmount));
    }
    public static long yuanToFen(BigDecimal yuanAmount){
        return (yuanAmount.multiply(new BigDecimal(100))).longValue();
    }


    /**
     * 金额 分转元
     * @param fenAmount
     * @return
     */
    public static String fenToYuan(String fenAmount){
        return (new BigDecimal(fenAmount).divide(new BigDecimal(100))).toString();
    }
    public static String fenToYuan(long fenAmount){
        return (new BigDecimal(fenAmount).divide(new BigDecimal(100))).toString();
    }

    /**
     * 两个字符串金额计算
     * @param amount1,amount2
     * @return String
     */
    public static String calculate(String amount1,String amount2,TYPE type){
        switch (type){
            case add: return (new BigDecimal(amount1).add(new BigDecimal(amount2))).toString();
            case subtract: return (new BigDecimal(amount1).subtract(new BigDecimal(amount2))).toString();
            case multiply: return (new BigDecimal(amount1).multiply(new BigDecimal(amount2))).toString();
            case divide: return (new BigDecimal(amount1).divide(new BigDecimal(amount2))).toString();
        }
        return null;
    }

    public enum TYPE {
        add, subtract, multiply, divide;
    }
    /**
     * 两个元单位字符串金额计算并且结果转分
     * @param amount1,amount2
     * @return long
     */
    public static long calculateAndToFen(String amount1,String amount2,TYPE type){
        return yanToFen(calculate(amount1,amount2,type));
    }

    /**
     * 两个分单位金额计算
     * @param amount1,amount2
     * @return
     */
    public static long calculateFen(long amount1,long amount2,TYPE type){
        switch (type){
            case add: return (new BigDecimal(amount1).add(new BigDecimal(amount2))).longValue();
            case subtract: return (new BigDecimal(amount1).subtract(new BigDecimal(amount2))).longValue();
            case multiply: return (new BigDecimal(amount1).multiply(new BigDecimal(amount2))).longValue();
            case divide: return (new BigDecimal(amount1).divide(new BigDecimal(amount2))).longValue();
        }
        return 0;
    }

    /**
     * 判断金额大小
     * amount1 > amount2 return true
     * @param amount1
     * @param amount2
     * @return
     */
    public static boolean gt(String amount1,String amount2){
        return new BigDecimal(amount1).compareTo(new BigDecimal(amount2)) > 0;
    }
    public static boolean gt(double amount1,double amount2){
        return new BigDecimal(amount1).compareTo(new BigDecimal(amount2)) > 0;
    }
    /**
     * 判断金额大小
     * amount1 >= amount2 return true
     * @param amount1
     * @param amount2
     * @return
     */
    public static boolean gte(String amount1,String amount2){
        return new BigDecimal(amount1).compareTo(new BigDecimal(amount2)) > -1;
    }

    /**
     * 元为单位的金额格式化
     * @param amount
     * @return 单位为元的带金额格式的字符串
     */
    public static String yuanFormat(String amount){
        DecimalFormat decimalFormat = new DecimalFormat("###,##0.00");
        return decimalFormat.format(Double.parseDouble(amount));
    }

    /**
     * 分为单位的金额格式化
     * @param fen String类型
     * @return 单位为元的带金额格式的字符串
     */
    public static String fenFormat(String fen){
        DecimalFormat decimalFormat = new DecimalFormat("###,##0.00");
        return decimalFormat.format(Double.parseDouble(fenToYuan(fen)));
    }
    /**
     * 分为单位的金额格式化
     * @param fen Long类型
     * @return 单位为元的带金额格式的字符串
     */
    public static String fenFormat(Long fen){
        DecimalFormat decimalFormat = new DecimalFormat("###,##0.00");
        return decimalFormat.format(Double.parseDouble(fenToYuan(fen)));
    }

}
