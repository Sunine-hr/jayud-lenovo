package com.jayud.po;

import com.jayud.enums.FormIDEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * k3组织架机构实体
 * @author bocong.zheng
 */
@Data
@K3Entity(value = {"FOrgID","FNumber","FName"},formId = FormIDEnum.ORGANIZATION)
public class Organization implements Serializable{

    private static final long serialVersionUID = -5337970767904200450L;
    /**
     * 组织机构ID
     */
    private Long FOrgID;

    /**
     * 组织机构编码
     */
    private String FNumber;

    /**
     * 组织机构名称
     */
    private String FName;

}
