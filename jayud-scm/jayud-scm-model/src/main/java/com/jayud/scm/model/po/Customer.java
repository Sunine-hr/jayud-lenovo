package com.jayud.scm.model.po;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@ApiModel(value="Customer对象", description="客户表")
public class Customer extends Model<Customer> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自动id")
      @TableId(value = "id", type = IdType.AUTO)
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

    @ApiModelProperty(value = "法人")
    private String legalDeputy;

    @ApiModelProperty(value = "注册地址")
    private String regAddress;

    @ApiModelProperty(value = "注册电话")
    private String regTel;

    @ApiModelProperty(value = "原税务编号，现改为纳税识别号")
    private String taxNo;

    @ApiModelProperty(value = "网址")
    private String companyNet;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "县、区")
    private String county;

    private Integer crtBy;

    private String crtByName;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime crtDtm;

    private Integer mdyBy;

    private String mdyByName;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime mdyDtm;

    private Integer voidedBy;

    private String voidedByName;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime voidedDtm;

    private Integer voided;

    @ApiModelProperty(value = "省名称")
    private String provinceName;

    @ApiModelProperty(value = "市名称")
    private String cityName;

    @ApiModelProperty(value = "县区名称")
    private String countyName;

    @ApiModelProperty(value = "审核状态")
    private String checkStateFlag;

    @ApiModelProperty(value = "审核级别")
    private Integer fLevel;

    @ApiModelProperty(value = "审核步骤")
    private Integer fStep;

    @ApiModelProperty(value = "客户拼音简写")
    private String customerAbbrCn;

    @ApiModelProperty(value = "跟单商务id")
    private Integer followerId;

    @ApiModelProperty(value = "业务员姓名")
    private String fsalesMan;

    @ApiModelProperty(value = "业务员id")
    private Integer fsalesId;

    @ApiModelProperty(value = "开票地址")
    private String invoiceAddress;

    @ApiModelProperty(value = "开票银行")
    private String invoiceBrank;

    @ApiModelProperty(value = "客户协议编号")
    private String customerAgNo;

    @ApiModelProperty(value = "原纳税识别号，现已被tax_no取代")
    private String taxIdentifyNumber;

    @ApiModelProperty(value = "开票抬头")
    private String makeUp;

    @ApiModelProperty(value = "开票账号")
    private String invoiceAccount;

    @ApiModelProperty(value = "开票电话")
    private String invoiceTel;

    @ApiModelProperty(value = "英文名称")
    private String enCustomerName;

    @ApiModelProperty(value = "英文地址")
    private String enCustomerAddress;

    @ApiModelProperty(value = "抬头方式")
    private String makeUpStyle;

    @ApiModelProperty(value = "客户类型(融资客户/垫资客户/账期客户)")
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

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "工商注册号")
    private String busRegisterNo;

    @ApiModelProperty(value = "组织机构代码")
    private String orgCode;

    @ApiModelProperty(value = "统一信用代码")
    private String unCreditCode;

    @ApiModelProperty(value = "公司类型")
    private String companyType;

    @ApiModelProperty(value = "行业")
    private String industry;

    @ApiModelProperty(value = "营业期限")
    private String businessTime;

    @ApiModelProperty(value = "核准日期")
    private String approvalDtm;

    @ApiModelProperty(value = "登记机")
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

    private Integer isCommitBms;

    @ApiModelProperty(value = "结算客户编码，用于凭证")
    private String k3AccNo;

    @ApiModelProperty(value = "客户，用于凭证")
    private String k3AccNo2;

    @ApiModelProperty(value = "区域")
    private String area;

    @ApiModelProperty(value = "是否共享客户")
    private Integer isShare;

    @ApiModelProperty(value = "代理品牌")
    private String agencyBrand;

    @ApiModelProperty(value = "供应商")
    private String supplierName;

    @ApiModelProperty(value = "经营产品")
    private String businessProducts;

    @ApiModelProperty(value = "进口量")
    private BigDecimal iAchievement;

    @ApiModelProperty(value = "出口量")
    private BigDecimal eAchievement;

    @ApiModelProperty(value = "客户来源")
    private String customerFrom;

    @ApiModelProperty(value = "目前合作供应链")
    private String supplyChain;

    @ApiModelProperty(value = "第一单下单日期")
    private LocalDate firstOrderDate;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
