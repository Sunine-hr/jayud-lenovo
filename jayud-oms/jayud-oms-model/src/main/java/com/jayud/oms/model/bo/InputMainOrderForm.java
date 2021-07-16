package com.jayud.oms.model.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.StringUtils;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class InputMainOrderForm {


    @ApiModelProperty(value = "主订单ID,编辑时必传")
    private Long orderId;

    @ApiModelProperty(value = "客户code")
    private String orderNo;

    @ApiModelProperty(value = "客户code", required = true)
    private String customerCode;

    @ApiModelProperty(value = "客户", required = true)
    private String customerName;

    @ApiModelProperty(value = "业务员ID", required = true)
    private Long bizUid;

    @ApiModelProperty(value = "业务员", required = true)
    private String bizUname;

    @ApiModelProperty(value = "合同编号")
    private String contractNo;

    @ApiModelProperty(value = "接单法人", required = true)
    private String legalName;

    @ApiModelProperty(value = "接单法人ID", required = true)
    private Long legalEntityId;

    @ApiModelProperty(value = "业务所属部门", required = true)
    private Long bizBelongDepart;

    @ApiModelProperty(value = "客户参考号")
    private String referenceNo;

    @ApiModelProperty(value = "业务类型", required = true)
    private String bizCode;

    @ApiModelProperty(value = "产品类型 如CBG:纯报关", required = true)
    private String classCode;

    @ApiModelProperty(value = "已选中得服务", required = true)
    private String selectedServer;

    @ApiModelProperty(value = "结算单位code", required = true)
    private String unitCode;

    @ApiModelProperty(value = "结算单位", required = true)
    private String unitAccount;

    @ApiModelProperty(value = "报关资料是否齐全 1-齐全 0-不齐全", required = true)
    private String isDataAll;

    @ApiModelProperty(value = "操作指令,前台忽略")
    private String cmd;

    @ApiModelProperty(value = "审核状态")
    private Integer status;

    @ApiModelProperty(value = "主订单备注")
    private String remarks;

    @ApiModelProperty(value = "创建人的类型(0:本系统,1:vivo.... 参照CreateUserTypeEnum)")
    private Integer createUserType;

    @ApiModelProperty(value = "操作时间")
    private String operationTime;

    @ApiModelProperty(value = "是否驳回操作")
    @JsonIgnore
    private Boolean isRejected;

    @ApiModelProperty(value = "驳回描述")
    @JsonIgnore
    private String rejectComment;


    @ApiModelProperty(value = "下一个订单状态")
    @JsonIgnore
    private Integer nextStatus;

    public void checkCreateOrder() {
        if (StringUtil.isNullOrEmpty(this.getCustomerCode())
                || StringUtil.isNullOrEmpty(this.getCustomerName())
                || this.getBizUid() == null
                || StringUtil.isNullOrEmpty(this.getBizUname())
                || StringUtil.isNullOrEmpty(this.getLegalName())
                || this.getLegalEntityId() == null
                || this.getBizBelongDepart() == null
                || StringUtil.isNullOrEmpty(this.getBizCode())
                || StringUtil.isNullOrEmpty(this.getClassCode())
                || StringUtil.isNullOrEmpty(this.getSelectedServer())
                || StringUtil.isNullOrEmpty(this.getUnitCode())
                || StringUtil.isNullOrEmpty(this.getUnitAccount())
                || (StringUtil.isNullOrEmpty(this.getIsDataAll())
                && this.getSelectedServer().contains(OrderStatusEnum.CKBG.getCode()))) {
            throw new JayudBizException(ResultEnum.PARAM_ERROR);
        }

        if (StringUtils.isEmpty(this.operationTime)) {
            throw new JayudBizException("请输入操作时间");
        }
    }

    public void specialTreatmentSelectedServer() {
        if (selectedServer.contains(OrderStatusEnum.XGQG.getCode())) {
            this.selectedServer = selectedServer + "," + OrderStatusEnum.ZGYSDD.getCode();
        }
    }

}
