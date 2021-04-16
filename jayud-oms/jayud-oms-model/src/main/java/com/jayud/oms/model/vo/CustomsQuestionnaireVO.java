package com.jayud.oms.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.enums.CreditStatusEnum;
import com.jayud.common.enums.CustomerTypeEnum;
import com.jayud.common.enums.CustomsCreditRatingEnum;
import com.jayud.oms.model.enums.CustomerRiskLevelEnum;
import com.jayud.oms.model.enums.CustomsQuestionnaireStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 海关调查问卷
 * </p>
 *
 * @author LDR
 * @since 2021-04-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "CustomsQuestionnaire对象", description = "海关调查问卷")
public class CustomsQuestionnaireVO extends Model<CustomsQuestionnaireVO> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "客户代码")
    private String customerCode;

    @ApiModelProperty(value = "客户地址")
    private String customerAddr;

    @ApiModelProperty(value = "法定代表人")
    private String legalRepresentative;

    @ApiModelProperty(value = "客户类型")
    private Integer customerType;

    @ApiModelProperty(value = "客户类型")
    private String customerTypeDesc;

    @ApiModelProperty(value = "注册资本")
    private BigDecimal registeredCapital;

    @ApiModelProperty(value = "联系人电话")
    private String phone;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "国家征信(0:异常,1:非异常)")
    private Integer nationalCredit;

    @ApiModelProperty(value = "国家征信(0:异常,1:非异常)")
    private String nationalCreditDesc;

    @ApiModelProperty(value = "海关征信(0:异常,1:非异常)")
    private Integer customsCredit;

    @ApiModelProperty(value = "海关征信(0:异常,1:非异常)")
    private String customsCreditDesc;

    @ApiModelProperty(value = "承诺付款账期")
    private String paymentPeriod;

    @ApiModelProperty(value = "是否为自理记账")
    private Boolean selfAccounting;

    @ApiModelProperty(value = "是否装警报系统(0:达标,1:部分达标,2:不达标)")
    private Integer isAlarmSystem;

    @ApiModelProperty(value = "是否装员工识别系统(0:达标,1:部分达标,2:不达标)")
    private Integer isEmpeIdenSys;

    @ApiModelProperty(value = "是否有要求工作交接(0:达标,1:部分达标,2:不达标)")
    private Integer isWorkHandover;

    @ApiModelProperty(value = "是否对商业伙伴进行全面评价(0:达标,1:部分达标,2:不达标)")
    private Integer isBusPartnerCompEval;

    @ApiModelProperty(value = "是否核对货物与单证信息的一致性(0:达标,1:部分达标,2:不达标)")
    private Integer isGoodsIdentical;

    @ApiModelProperty(value = "是否提供车辆及司机信息(0:达标,1:部分达标,2:不达标)")
    private Integer isProvideVehicleInfo;

    @ApiModelProperty(value = "风险等级(1:不可接受之信用风险,2:高度信用风险,3:中度信用风险,4:低信用风险)")
    private Integer riskLevel;

    @ApiModelProperty(value = "风险等级(1:不可接受之信用风险,2:高度信用风险,3:中度信用风险,4:低信用风险)")
    private String riskLevelDesc;

    @ApiModelProperty(value = "记录人")
    private String recorder;

    @ApiModelProperty(value = "评估日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private LocalDateTime evaluationDate;

    @ApiModelProperty(value = "失效时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private LocalDateTime expiresTime;

    @ApiModelProperty(value = "状态(0:待审核,1:经理审核,2:总经理审核)")
    private Integer status;

    @ApiModelProperty(value = "状态(0:待审核,1:经理审核,2:总经理审核)")
    private String statusDesc;

    @ApiModelProperty(value = "类型(0:客户,1:供应商)")
    private Integer type;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "订单状态")
    private String orderStatus;

    @ApiModelProperty(value = "审核意见")
    private String auditOpinion;


    public void setCustomerType(Integer customerType) {
        this.customerType = customerType;
        this.customerTypeDesc = CustomerTypeEnum.getDesc(customerType);
    }

    public void setNationalCredit(Integer nationalCredit) {
        this.nationalCredit = nationalCredit;
        this.nationalCreditDesc = CreditStatusEnum.getDesc(nationalCredit);
    }

    public void setCustomsCredit(Integer customsCredit) {
        this.customsCredit = customsCredit;
        this.customsCreditDesc = CreditStatusEnum.getDesc(customsCredit);
    }

    public void setRiskLevel(Integer riskLevel) {
        this.riskLevel = riskLevel;
        this.riskLevelDesc = CustomerRiskLevelEnum.getDesc(riskLevel);
    }


    public void setStatus(Integer status) {
        this.status = status;
        //0:待审核,1:经理审核,2:总经理审核
        switch (status) {
            case 0:
                statusDesc = "待审核";
                break;
            case 1:
                statusDesc = "经理审核";
                break;
            case 2:
                statusDesc = "总经理审核";
                break;
            default:
                statusDesc = "待审核";
        }
    }


    public String getOrderStatus() {
        LocalDateTime dateOne = this.evaluationDate.plusMonths(11);
        LocalDateTime dateTwo = this.expiresTime;
        LocalDateTime now = LocalDateTime.now();
        if (now.compareTo(dateOne) >= 0 &&
                LocalDateTime.now().compareTo(dateTwo) < 0) {
            return CustomsQuestionnaireStatusEnum.TWO.getDesc();
        }
        if (now.compareTo(dateTwo) >= 0) {
            return CustomsQuestionnaireStatusEnum.THREE.getDesc();
        }
        return CustomsQuestionnaireStatusEnum.ONE.getDesc();
    }


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
