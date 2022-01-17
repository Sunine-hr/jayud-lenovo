package com.jayud.scm.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
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
 * @since 2021-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AddCustomerForm {

    @ApiModelProperty(value = "自动id")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "编号")
    private String customerNo;

    @ApiModelProperty(value = "名称")
    @NotNull(message = "客户名称不为空")
    private String customerName;

    @ApiModelProperty(value = "客户等级")
    private String customerType;

    @ApiModelProperty(value = "简称")
//    @NotNull(message = "客户简称不为空")
    private String customerAbbr;

//    @ApiModelProperty(value = "跟单商务id")
//    private Integer followerId;
//
//    @ApiModelProperty(value = "商务")
//    private String followerName;
//
//    @ApiModelProperty(value = "业务员id")
//    private Integer fsalesId;
//
//    @ApiModelProperty(value = "业务员姓名")
//    private String fsalesMan;

//    @ApiModelProperty(value = "是否共享客户")
//    private Integer isShare;

    @ApiModelProperty(value = "法人")
    private String legalDeputy;

    @ApiModelProperty(value = "注册地址")
    private String regAddress;

    @ApiModelProperty(value = "注册电话")
    private String regTel;

    @ApiModelProperty(value = "税务编号，纳税识别号，统一信用号码")
    private String taxNo;

    @ApiModelProperty(value = "海关代码")
//    @NotNull(message = "海关代码不为空")
    private String customsCode;

    @ApiModelProperty(value = "类型")
//    @NotNull(message = "类型不为空")
    private String type;

    @ApiModelProperty(value = "网址")
    private String companyNet;

    @ApiModelProperty(value = "省名称")
    private String provinceName;

    @ApiModelProperty(value = "市名称")
    private String cityName;

    @ApiModelProperty(value = "县区名称")
    private String countyName;

    @ApiModelProperty(value = "客户拼音简写")
    private String customerAbbrCn;

    @ApiModelProperty(value = "英文名称")
    private String enCustomerName;

    @ApiModelProperty(value = "英文地址")
    private String enCustomerAddress;

    @ApiModelProperty(value = "客户类型(融资客户/垫资客户/账期客户)")
//    @NotNull(message = "客户类型不为空")
    private String customerStyle;

    @ApiModelProperty(value = "传真")
    private String regFax;

    @ApiModelProperty(value = "在线账户")
    private String loginName;

    @ApiModelProperty(value = "在线id")
    private String onlId;

    @ApiModelProperty(value = "在线编号")
    private String onlNo;

    @ApiModelProperty(value = "0未同步1已同步")
    private Integer isSync;

    @ApiModelProperty(value = "客户跟进状态")
    private String customerState;

    @ApiModelProperty(value = "已付款未收货总额度")
    private BigDecimal payLimit;

    @ApiModelProperty(value = "是否自动核销，0：不自动核销，1：自动核销")
    private Integer isVerificationFlag;

    @ApiModelProperty(value = "工商注册号")
    private String busRegisterNo;

    @ApiModelProperty(value = "组织机构代码")
    private String orgCode;

    @ApiModelProperty(value = "公司类型")
    private String companyType;

    @ApiModelProperty(value = "行业")
    private String industry;

    @ApiModelProperty(value = "营业期限")
    private String businessTime;

    @ApiModelProperty(value = "核准日期")
    private String approvalDtm;

    @ApiModelProperty(value = "登记机关")
    private String registerMac;

    @ApiModelProperty(value = "经营范围")
    private String operScope;

    @ApiModelProperty(value = "注册资本")
    private String regCapital;

    @ApiModelProperty(value = "注册时间")
    private String regTime;

    @ApiModelProperty(value = "公司状态")
    private String companyStatus;

    @ApiModelProperty(value = "提交到wms，0未提交1已提交")
    private Integer isCommitWms;

    @ApiModelProperty(value = "提交到bms，0未提交1已提交")
    private Integer isCommitBms;

    @ApiModelProperty(value = "区域")
//    @NotNull(message = "区域不为空")
    private String area;

    @ApiModelProperty(value = "代理品牌")
    private String agencyBrand;

    @ApiModelProperty(value = "主要供应商")
    private String supplierName;

    @ApiModelProperty(value = "经营产品")
    private String businessProducts;

    @ApiModelProperty(value = "预计进口量")
    @JsonProperty(value = "iAchievement")
    private BigDecimal iAchievement;

    @ApiModelProperty(value = "预计出口量")
    @JsonProperty(value = "eAchievement")
    private BigDecimal eAchievement;

    @ApiModelProperty(value = "客户来源")
//    @NotNull(message = "客户来源不为空")
    private String customerFrom;

    @ApiModelProperty(value = "目前合作供应链")
    private String supplyChain;

    @ApiModelProperty(value = "备注")
    private String remark;


}
