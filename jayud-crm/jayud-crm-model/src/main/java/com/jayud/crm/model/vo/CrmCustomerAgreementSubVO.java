package com.jayud.crm.model.vo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.jayud.common.entity.SysBaseEntity;
import com.jayud.crm.model.po.CrmFile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;

/**
 * CrmCustomerAgreementSub 实体类
 *
 * @author jayud
 * @since 2022-03-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="基本档案_协议管理_子协议(crm_customer_agreement_sub)对象", description="基本档案_协议管理_子协议(crm_customer_agreement_sub)")
public class CrmCustomerAgreementSubVO extends SysBaseEntity {


    @ApiModelProperty(value = "主协议ID")
    private Long pId;

    @ApiModelProperty(value = "协议编号")
    private String agreementCode;

    @ApiModelProperty(value = "客户ID")
    private Integer custId;

    @ApiModelProperty(value = "客户名称")
    private String custName;

    @ApiModelProperty(value = "业务类型(报关，货代，物流等)")
    private String businessType;

    @ApiModelProperty(value = "业务类型值")
    private String businessValue;

    @ApiModelProperty(value = "协议类型")
    private String treatyType;

    @ApiModelProperty(value = "协议名称")
    private String treatyName;

    @ApiModelProperty(value = "协议开始日期")
    private LocalDate beginDate;

    @ApiModelProperty(value = "协议结束日期")
    private LocalDate endDate;

    @ApiModelProperty(value = "协议有效期")
    private Integer validityPeriod;

    @ApiModelProperty(value = "是否顺延")
    private Boolean isExtended;

    @ApiModelProperty(value = "顺延天数(1:顺延6个月,2:顺延12个月,3:顺延24个月)")
    private Integer postponedType;

    @ApiModelProperty(value = "协议版本")
    private String treatyCopy;

    @ApiModelProperty(value = "协议说明")
    private String treatyRemark;

    @ApiModelProperty(value = "销售员id")
    private Long userId;

    @ApiModelProperty(value = "销售员")
    private String user;

    @ApiModelProperty(value = "报价单id")
    private Long quotationId;

    @ApiModelProperty(value = "关联报价单号")
    private String quotationNum;

    @ApiModelProperty(value = "法人主体id")
    private Long departId;

    @ApiModelProperty(value = "法人主体名称")
    private String departName;

    @ApiModelProperty(value = "我司原件份数")
    private Boolean filesNum;

    @ApiModelProperty(value = "是否默认协议")
    private Boolean isDefault;

    @ApiModelProperty(value = "是否归档")
    private Boolean isFiles;

    @ApiModelProperty(value = "归档编号")
    private String filesCode;

    @ApiModelProperty(value = "归档人")
    private String filesUser;

    @ApiModelProperty(value = "归档时间")
    private LocalDate filesDtm;

    @ApiModelProperty(value = "审核级别")
    private Integer fLevel;

    @ApiModelProperty(value = "当前级别")
    private Integer fStep;

    @ApiModelProperty(value = "审核状态")
    private String checkStateFlag;

    @ApiModelProperty(value = "流程实例")
    private Integer flowInstanceId;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;

    @ApiModelProperty(value = "协议时间")
    private List<LocalDate> agreementTime;

    @ApiModelProperty(value = "上传文件")
    private List<CrmFile> files;

    @ApiModelProperty(value = "协议时间")
    private String effectiveTime;

}
