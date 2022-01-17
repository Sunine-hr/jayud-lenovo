package com.jayud.scm.model.po;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 客户明细表
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="CustomerEntry对象", description="客户明细表")
public class CustomerEntry extends Model<CustomerEntry> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自动id")
    @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

    @ApiModelProperty(value = "英文名称")
    private String customerNameEn;

    @ApiModelProperty(value = "成立时间")
    private LocalDateTime establishDate;

    @ApiModelProperty(value = "经营期限")
    private String operatingPeriod;

    @ApiModelProperty(value = "实际控制人")
    private String realMan;

    @ApiModelProperty(value = "股东")
    private String shareholder;

    @ApiModelProperty(value = "控股比例")
    private String holdingRatio;

    @ApiModelProperty(value = "经营范围")
    private String businessScope;

    @ApiModelProperty(value = "注册类型")
    private String registrationType;

    @ApiModelProperty(value = "注册资本")
    private BigDecimal registrationCapitale;

    @ApiModelProperty(value = "实收资本")
    private BigDecimal paidCapital;

    @ApiModelProperty(value = "注册资本币别")
    private String registrationCurrency;

    @ApiModelProperty(value = "营业地址")
    private String businessAddress;

    @ApiModelProperty(value = "企业性质")
    private String enterpriseNature;

    @ApiModelProperty(value = "税务类别")
    private String taxCategory;

    @ApiModelProperty(value = "认定时间")
    private LocalDateTime thatDate;

    @ApiModelProperty(value = "纳税识别号")
    private String taxIdentifyNumber;

    @ApiModelProperty(value = "开票抬头")
    private String makeUp;

    @ApiModelProperty(value = "合作日期")
    private LocalDateTime cooperationDate;

    @ApiModelProperty(value = "海关代码")
    private String customsCode;

    @ApiModelProperty(value = "海关信用级别")
    private String customsCreditLevel;

    @ApiModelProperty(value = "统一社会信用代码/注册号")
    private String socialCreditCode;

    @ApiModelProperty(value = "所属地区")
    private String localArea;

    @ApiModelProperty(value = "合作产品")
    private String cooperativeProduct;

    @ApiModelProperty(value = "主要经营数据")
    private String businessData;

    private Integer crtBy;

    private String crtByName;

    private LocalDateTime crtDtm;

    private Integer mdyBy;

    private String mdyByName;

    private LocalDateTime mdyDtm;

    private Integer voided;

    private Integer voidedBy;

    private String voidedByName;

    private LocalDateTime voidedDtm;

    @ApiModelProperty(value = "客户id")
    private Integer customerId;

    @ApiModelProperty(value = "注册地址")
    private String registrationAddress;

    @ApiModelProperty(value = "组织机构代码")
    private String organizationCode;

    @ApiModelProperty(value = "境内货源地")
    private String originArea;

    @ApiModelProperty(value = "香港抬头")
    private String hkCompanyName;

    @ApiModelProperty(value = "香港抬头id")
    private Integer hkCompanyId;

    @ApiModelProperty(value = "是否同用报关合同号")
    private String useContractNo;

    @ApiModelProperty(value = "检验检疫编码")
    private String ciqCode;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
