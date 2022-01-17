package com.jayud.scm.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LegalEntityVO {

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "法人主体")
    private String legalName;

    @ApiModelProperty(value = "主体编号")
    private String legalCode;

    @ApiModelProperty(value = "注册所在地")
    private String rigisAddress;

    @ApiModelProperty(value = "销售部门")
    private String saleDepartName;

    @ApiModelProperty(value = "销售部门ID")
    private Long saleDepartId;

    @ApiModelProperty(value = "审核状态")
    private Long auditStatus;

    @ApiModelProperty(value = "审核状态描述")
    private String auditStatusDesc;

    @ApiModelProperty(value = "审核意见")
    private String auditComment;


    @ApiModelProperty(value = "英文名")
    private String legalEnName;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "传真")
    private String fax;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "开户银行")
    private String bank;

    @ApiModelProperty(value = "开户账户")
    private String accountOpen;

    @ApiModelProperty(value = "纳税识别号")
    private String taxIdentificationNum;

}
