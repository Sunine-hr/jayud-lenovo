package com.jayud.scm.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 客户表
 * </p>
 *
 * @author LLJ
 * @since 2021-07-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CustomerFormVO {

//    @ApiModelProperty(value = "自动id")
//      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "编号")
    private String customerNo;

    @ApiModelProperty(value = "名称")
    private String customerName;

    @ApiModelProperty(value = "类型")
    private String customerType;

    @ApiModelProperty(value = "简称")
    private String customerAbbr;

    @ApiModelProperty(value = "商务")
    private String followerName;

    @ApiModelProperty(value = "业务类型")
    private String businessType;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "法人")
    private String legalDeputy;

    @ApiModelProperty(value = "注册地址")
    private String regAddress;

    @ApiModelProperty(value = "注册电话")
    private String regTel;

    @ApiModelProperty(value = "纳税识别号")
    private String taxNo;

    @ApiModelProperty(value = "网址")
    private String companyNet;

    @ApiModelProperty(value = "省")
    private String provinceName;

    @ApiModelProperty(value = "市")
    private String cityName;

    @ApiModelProperty(value = "县、区")
    private String countyName;

    @ApiModelProperty(value = "创建人")
    private String crtByName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime crtByDtm;

    @ApiModelProperty(value = "修改人")
    private String mdyByName;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime mdyByDtm;

    @ApiModelProperty(value = "审核状态")
    private String checkStateFlag;

    @ApiModelProperty(value = "业务员姓名")
    private String fsalesMan;

    @ApiModelProperty(value = "客户协议编号")
    private String customerAgNo;


    @ApiModelProperty(value = "英文名称")
    private String enCustomerName;

    @ApiModelProperty(value = "英文地址")
    private String enCustomerAddress;

    @ApiModelProperty(value = "客户类型")
    private String customerStyle;

    @ApiModelProperty(value = "客户跟进状态")
    private String customerState;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "区域")
    private String area;


}
