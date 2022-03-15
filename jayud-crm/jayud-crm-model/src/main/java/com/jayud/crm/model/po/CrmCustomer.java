package com.jayud.crm.model.po;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.format.annotation.DateTimeFormat;

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
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String unCreditCode;

    @ApiModelProperty(value = "企业跟进状态(潜在，意向，合作，暂不合作）")
    private String custState;

    @ApiModelProperty(value = "是否普通用户")
    private Boolean isCust;

    @ApiModelProperty(value = "是否公海客户")
    private Boolean isPublic;

    @ApiModelProperty(value = "是否供应商")
    private Boolean isSupplier;

    @NotBlank(message = "客户简称不能为空")
    @ApiModelProperty(value = "简称")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String custNameAbbr;

    @ApiModelProperty(value = "英文名称")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String custNameEn;

    @ApiModelProperty(value = "企业类型(融资客户，账期客户，现结客户）")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String custStyle;

    @ApiModelProperty(value = "客户等级(A,AA,AAA等）")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String custLevel;

    @ApiModelProperty(value = "行业类型")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
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

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String regOrg;

    @ApiModelProperty(value = "海关代码")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String customsCode;

    @ApiModelProperty(value = "商检代码")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String ciqCode;

    @ApiModelProperty(value = "客户来源")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String custForm;

    @ApiModelProperty(value = "目前合作方")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String supplyChain;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "第一单日期")
    private LocalDateTime firstOrderDate;

    @ApiModelProperty(value = "业务ID")
    private Long fsalesId;

    @ApiModelProperty(value = "业务员名称")
    private String fsalesName;

    @ApiModelProperty(value = "商务ID")
    private Long followerId;

    @ApiModelProperty(value = "商务名称")
    private String followerName;

    @ApiModelProperty(value = "在线系统ID")
    private Integer onlId;

    @ApiModelProperty(value = "在线系统编号")
    private String onlNo;

    @ApiModelProperty(value = "是否同步")
    private Boolean isSync;

    @JsonProperty(value = "fLevel")
    @ApiModelProperty(value = "审核级别")
    private Integer fLevel;

    @JsonProperty(value = "fStep")
    @ApiModelProperty(value = "当前级别")
    private Integer fStep;

    @ApiModelProperty(value = "审核状态")
    private String checkStateFlag;

    @ApiModelProperty(value = "曾用名")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String oldCustName;

    @ApiModelProperty(value = "公司编码")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String custCode;

    @ApiModelProperty(value = "供应商编码")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String supplierCode;

    @ApiModelProperty(value = "业务类型")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String businessTypes;

    @ApiModelProperty(value = "服务类型")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String serviceType;

    @ApiModelProperty(value = "帐期")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String accountingPeriod;

    @ApiModelProperty(value = "对账方式")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String reconciliationMethod;

    @ApiModelProperty(value = "结算方式")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String settlementMethod;

    @ApiModelProperty(value = "所属行业")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String industry;

    @ApiModelProperty(value = "网址地址")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String websiteUrl;

    @ApiModelProperty(value = "企业信用")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String custEnterpriseCredit;

    @ApiModelProperty(value = "进口信用")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String custImportCredit;

    @ApiModelProperty(value = "海关信用")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String custCustomsCredit;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "转入公海时间")
    private LocalDateTime transferPublicTime;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;

    @TableField(exist = false)
    @ApiModelProperty(value = "是否不存在黑名单")
    private Boolean isNotInBlackList;

    @TableField(exist = false)
    @ApiModelProperty(value = "是否审核通过")
    private Boolean isApproved;


    @TableField(exist = false)
    @ApiModelProperty(value = "客户id集合")
    private List<Long> custIdList;



}
