package com.geyao.barbershop.common;

import com.geyao.barbershop.constants.BuziConstant;
import lombok.Data;


@Data
public class ResultVo {
    private int code;
    private String msg;
    private Object data;

    public ResultVo(){
    }

    public ResultVo(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResultVo(Object data){
        this.code = BuziConstant.SUCCESS_CODE;
        this.data = data;
    }

    public ResultVo(String msg){
        this.code = BuziConstant.FAIL_CODE;
        this.msg = msg;
    }
}
