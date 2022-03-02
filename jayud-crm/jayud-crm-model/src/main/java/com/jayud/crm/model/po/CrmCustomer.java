package com.jayud.crm.model.po;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import javax.validation.constraints.NotBlank;

/**
 * CrmCustomer 实体类
 *
 * @author jayud
 * @since 2022-03-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="基本档案_客户_基本信息(crm_customer)对象", description="基本档案_客户_基本信息(crm_customer)")
public class CrmCustomer extends SysBaseEntity {


    @ApiModelProperty(value = "(母公司ID)")
    private Long parentId;

    @NotBlank(message = "客户名称不能为空")
    @ApiModelProperty(value = "客户名称")
    private String custName;

    @ApiModelProperty(value = "统一信用代码")
    private String unCreditCode;

    @ApiModelProperty(value = "企业跟进状态(潜在，意向，合作，暂不合作）")
    private Boolean custState;

    @ApiModelProperty(value = "是否普通用户")
    private Boolean isCust;

    @ApiModelProperty(value = "是否公海客户")
    private Boolean isPublic;

    @ApiModelProperty(value = "是否供应商")
    private Boolean isSupplier;

    @NotBlank(message = "客户简称不能为空")
    @ApiModelProperty(value = "简称")
    private String custNameAbbr;

    @ApiModelProperty(value = "英文名称")
    private String custNameEn;

    @ApiModelProperty(value = "企业类型(融资客户，账期客户，现结客户）")
    private String custStyle;

    @ApiModelProperty(value = "客户等级(A,AA,AAA等）")
    private String custLevel;

    @ApiModelProperty(value = "行业类型")
    private String industryType;

    @ApiModelProperty(value = "国家")
    private String country;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "县、区")
    private String county;

    @ApiModelProperty(value = "区域(华南，华北，西南等)")
    private String area;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "注册时间")
    private LocalDateTime regDate;

    @ApiModelProperty(value = "注册地址")
    private String regAddr;

    @ApiModelProperty(value = "注册地址英文")
    private String regAddrEn;

    @ApiModelProperty(value = "注册电话")
    private String regTel;

    @ApiModelProperty(value = "企来类型(有限责任公司,上市公司，国有企业等)")
    private String custRegTpye;

    @ApiModelProperty(value = "登记注册号")
    private String custRegNumber;

    @ApiModelProperty(value = "组织机构代码")
    private String custOrgCode;

    @ApiModelProperty(value = "有效期截止")
    private String buzlicenseExpirdate;

    @ApiModelProperty(value = "法定代表人姓名")
    private String legalpersonName;

    @ApiModelProperty(value = "实际控制人")
    private String actualcontrolName;

    @ApiModelProperty(value = "是否一般纳税人")
    private Boolean isTaxpayer;

    @ApiModelProperty(value = "注册资本（万元）")
    private BigDecimal regCapital;

    @ApiModelProperty(value = "实缴资本（万元）")
    private BigDecimal paidCapital;

    @ApiModelProperty(value = "注册币别")
    private String regCurrency;

    @ApiModelProperty(value = "经营范围")
    private String operScope;

    @ApiModelProperty(value = "注册机构")
    private String regOrg;

    @ApiModelProperty(value = "海关代码")
    private String customsCode;

    @ApiModelProperty(value = "商检代码")
    private String ciqCode;

    @ApiModelProperty(value = "客户来源")
    private String custForm;

    @ApiModelProperty(value = "目前合作方")
    private String supplyChain;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "第一单日期")
    private LocalDateTime firstOrderDate;

    @ApiModelProperty(value = "业务ID")
    private Integer fsalesId;

    @ApiModelProperty(value = "业务员名称")
    private String fsalesName;

    @ApiModelProperty(value = "商务ID")
    private Integer followerId;

    @ApiModelProperty(value = "商务名称")
    private String followerName;

    @ApiModelProperty(value = "在线系统ID")
    private Integer onlId;

    @ApiModelProperty(value = "在线系统编号")
    private String onlNo;

    @ApiModelProperty(value = "是否同步")
    private Boolean isSync;

    @ApiModelProperty(value = "审核级别")
    private Integer fLevel;

    @ApiModelProperty(value = "当前级别")
    private Integer fStep;

    @ApiModelProperty(value = "审核状态")
    private String checkStateFlag;

    @ApiModelProperty(value = "曾用名")
    private String oldCustName;

    @ApiModelProperty(value = "公司编码")
    private String custCode;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "业务类型")
    private String businessTypes;

    @ApiModelProperty(value = "服务类型")
    private Integer serviceType;

    @ApiModelProperty(value = "帐期")
    private Integer accountingPeriod;

    @ApiModelProperty(value = "对账方式")
    private Integer reconciliationMethod;

    @ApiModelProperty(value = "结算方式")
    private Integer settlementMethod;

    @ApiModelProperty(value = "所属行业")
    private Integer industry;

    @ApiModelProperty(value = "网址地址")
    private String websiteUrl;

    @ApiModelProperty(value = "企业信用")
    private Integer custEnterpriseCredit;

    @ApiModelProperty(value = "进口信用")
    private Integer custImportCredit;

    @ApiModelProperty(value = "海关信用")
    private Integer custCustomsCredit;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;





}
