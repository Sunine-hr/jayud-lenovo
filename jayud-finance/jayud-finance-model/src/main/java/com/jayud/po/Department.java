package com.jayud.po;

import com.jayud.enums.FormIDEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * k3 基础资料-部门 实体
 * @author hugo
 */
@Data
@K3Entity(value = {"FDEPTID","FName","FNumber","FDescription","FUseOrgId","FUseOrgId.FNumber"},formId = FormIDEnum.DEPARTMENT)
public class Department implements Serializable{
    private static final long serialVersionUID = -2844937914575351260L;

    /**
     * 实体主键
     */
    private Long FDEPTID;

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

    /**
     * 使用组织编码
     */
    private String FUseOrgId_FNumber;

}
