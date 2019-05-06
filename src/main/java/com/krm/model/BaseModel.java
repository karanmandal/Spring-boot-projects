package com.krm.model;

import lombok.Data;

import java.util.Date;

@Data
public class BaseModel {

    protected Date createdAt;

    protected Date updateAt;

}
