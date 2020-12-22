package com.geyao.barbershop.deal.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Config implements Serializable {

    private String name;
    private String code;
    private String value;
    private String type;
}
