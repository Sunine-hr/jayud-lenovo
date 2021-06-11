package com.jayud.mall.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LegalEntityVO {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "法人主体")
    private String legalName;

    @ApiModelProperty(value = "主体编号")
    private String legalCode;

    @ApiModelProperty(value = "注册所在地")
    private String rigisAddress;

    @ApiModelProperty(value = "销售部门")
    private Long saleDepartId;

    @ApiModelProperty(value = "审核意见")
    private String auditComment;

    @ApiModelProperty(value = "审核状态 1-待审核 2-通过 0-拒绝")
    private Long auditStatus;

    @ApiModelProperty(value = "创建人")
    private String createdUser;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "修改人")
    private String updatedUser;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;

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

    @ApiModelProperty(value = "税号标识")
    private String taxIdentificationNum;

}
