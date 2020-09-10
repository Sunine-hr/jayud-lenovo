package com.jayud.po;

import com.jayud.enums.FormIDEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * k3客户表实体
 * @author bocong.zheng
 */

@Data
@K3Entity(value = {"FCUSTID","FNumber","FName"},formId = FormIDEnum.CUSTOMER)
public class Customer implements Serializable{

    private static final long serialVersionUID = -5076769214049275175L;
    /**
     * 客户表主键ID
     */
    private Long FCUSTID;


    /**
     * 客户编码
     */
    private String FNumber;

    /**
     * 客户名称
     */
    private String FName;

}
