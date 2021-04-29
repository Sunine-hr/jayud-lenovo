package com.jayud.oms.model.vo;

import com.jayud.common.enums.CreditStatusEnum;
import com.jayud.common.enums.CustomsCreditRatingEnum;
import com.jayud.oms.model.enums.CustomerInfoStatusEnum;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CustomerInfoVO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "一级客户名称")
    private String name;

    @ApiModelProperty(value = "客户代码")
    private String idCode;

    @ApiModelProperty(value = "客户类型(1同行 2电商 3货代)")
    private Integer types;

    @ApiModelProperty(value = "联系人")
    private String contact;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "法人主体拼接串,ID,ID")
    private String legalEntityIdStr;

    @ApiModelProperty(value = "法人主体集合")
    private List<Long> legalEntityIds = new ArrayList<>();

    @ApiModelProperty(value = "纳税号")
    private String tfn;

    @ApiModelProperty(value = "是否有合同(0否 1是)")
    private String ifContract;

    @ApiModelProperty(value = "结算类型(1票结 2月结 3周结)")
    private Integer settlementType;

    @ApiModelProperty(value = "账期")
    private String accountPeriod;

    @ApiModelProperty(value = "税票种类")
    private String taxType;

    @ApiModelProperty(value = "税率")
    private String taxRate;

    @ApiModelProperty(value = "等级")
    private Integer estate;

    @ApiModelProperty(value = "接单部门ID")
    private Long departmentId;

    @ApiModelProperty(value = "接单部门")
    private String departmentName;

    @ApiModelProperty(value = "接单客服ID")
    private Long kuId;

    @ApiModelProperty(value = "业务员ID")
    private Long ywId;

    @ApiModelProperty(value = "接单客服")
    private String kuName;

    @ApiModelProperty(value = "业务员")
    private String ywName;

    @ApiModelProperty(value = "审核状态")
    private Integer auditStatus;

    @ApiModelProperty(value = "审核状态描述")
    private String auditStatusDesc;

    @ApiModelProperty(value = "是否有效")
    private String status;

    @ApiModelProperty(value = "是否有效 Boolean类型")
    private Boolean statusDesc;

    @ApiModelProperty(value = "创建人")
    private String createdUser;

    @ApiModelProperty(value = "创建时间")
    private String createdTimeStr;

    @ApiModelProperty(value = "审核意见")
    private String auditComment;

    @ApiModelProperty(value = "更新时间")
    private String updatedTimeStr;

    @ApiModelProperty(value = "法人主体集合")
    private List<LegalEntityVO> legalEntities;

    @ApiModelProperty(value = "国家征信")
    private Integer nationalCredit;

    @ApiModelProperty(value = "海关征信")
    private Integer customsCredit;

    @ApiModelProperty(value = "海关信用等级(0:一般认证企业,1:一般信用企业,2:高级信用企业,3:失信企业)")
    private Integer customsCreditRating;

    @ApiModelProperty(value = "国家征信")
    private String nationalCreditDesc;

    @ApiModelProperty(value = "海关征信")
    private String customsCreditDesc;

    @ApiModelProperty(value = "海关信用等级(0:一般认证企业,1:一般信用企业,2:高级信用企业,3:失信企业)")
    private String customsCreditRatingDesc;

    @ApiModelProperty(value = "是否高级认证")
    private Boolean isAdvancedCertification;


    public List<Long> getLegalEntityIds() {
        if (!StringUtil.isNullOrEmpty(this.legalEntityIdStr)) {
            String[] strs = this.legalEntityIdStr.split(",");
            for (String str : strs) {
                legalEntityIds.add(Long.valueOf(str));
            }
        }
        return legalEntityIds;
    }


    public void setNationalCredit(Integer nationalCredit) {
        this.nationalCredit = nationalCredit;
        this.nationalCreditDesc = CreditStatusEnum.getDesc(nationalCredit);
    }

    public void setCustomsCredit(Integer customsCredit) {
        this.customsCredit = customsCredit;
        this.customsCreditDesc = CreditStatusEnum.getDesc(customsCredit);
    }

    public void setCustomsCreditRating(Integer customsCreditRating) {
        this.customsCreditRating = customsCreditRating;
        this.customsCreditRatingDesc = CustomsCreditRatingEnum.getDesc(customsCreditRating);
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
        this.auditStatusDesc = CustomerInfoStatusEnum.getDesc(auditStatus.toString());
    }

    public Boolean getStatusDesc() {
        if ("1".equals(this.status)) {
            return true;
        }
        return false;
    }
}
