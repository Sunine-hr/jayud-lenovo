package com.jayud.finance.po;

import com.jayud.finance.enums.FormIDEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * k3 基础资料-物料 实体
 * @author bocong.zheng
 */
@Data
@K3Entity(value = {"FMATERIALID","FName","FNumber","FDescription","FUseOrgId"},formId = FormIDEnum.MATERIAL)
public class Material implements Serializable{
    private static final long serialVersionUID = -4043372624701027480L;

    /**
     * 实体主键
     */
    private Long FMATERIALID;

    /**
     * 名称
     */
    private String FName;

    /**
     * 编码
     */
    private String FNumber;

    /**
     * 描述
     */
    private String FDescription;

    /**
     * 使用组织
     */
    private Long FUseOrgId;
}
