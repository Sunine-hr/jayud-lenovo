package com.jayud.customs.model.vo;

import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.utils.FileView;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class CustomsOrderInfoVO {

    @ApiModelProperty(value = "子订单ID")
    private Integer id;

    @ApiModelProperty(value = "订单号")
    private Long mainOrderId;

    @ApiModelProperty(value = "订单号")
    private String mainOrderNo;

    @ApiModelProperty(value = "子订单")
    private String orderNo;

    @ApiModelProperty(value = "进出口类型")
    private Integer goodsType;

    @ApiModelProperty(value = "进出口类型")
    private String goodsTypeDesc;

    @ApiModelProperty(value = "通关口岸")
    private String portName;

    @ApiModelProperty(value = "状态CODE")
    private String status;

    @ApiModelProperty(value = "状态描述")
    private String statusDesc;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "客户名称code")
    private String customerCode;

    @ApiModelProperty(value = "法人主体")
    private String legalName;

    @ApiModelProperty(value = "结算单位")
    private String unitCode;

    @ApiModelProperty(value = "子订单法人主体")
    private String subLegalName;

    @ApiModelProperty(value = "子订单结算单位")
    private String subUnitCode;

    @ApiModelProperty(value = "费用录用默认结算单位")
    private String defaultUnitCode;

    @ApiModelProperty(value = "货物信息")
    private String goodsInfo;

    @ApiModelProperty(value = "六联单号")
    private String encode;

    @ApiModelProperty(value = "报关文件")
    private List<FileView> fileViews;

    @ApiModelProperty(value = "创建人")
    private String createdUser;

    @ApiModelProperty(value = "创建时间")
    private String createdTimeStr;

    @ApiModelProperty(value = "附件")
    private String fileStr;

    @ApiModelProperty(value = "附件")
    private String fileNameStr;

    @ApiModelProperty(value = "委托号")
    private String entrustNo;

    @ApiModelProperty(value = "统一编号")
    private String unifiedNumber;

    @ApiModelProperty(value = "更新时间")
    private String updatedTimeStr;

    @ApiModelProperty(value = "审核意见")
    private String remarks;

    @ApiModelProperty(value = "报关单号")
    private String yunCustomsNo;

    @ApiModelProperty(value = "业务类型")
    private String bizCode;

    @ApiModelProperty(value = "作业类型")
    private String classCode;

    @ApiModelProperty(value = "作业类型")
    private String classCodeDesc;

    @ApiModelProperty(value = "是否有费用详情")
    private boolean isCost;

    @ApiModelProperty(value = "是否需要录入费用")
    private Boolean needInputCost;

    @ApiModelProperty(value = "过磅数KG")
    private Double carWeighNum;

    public void setGoodsTypeDesc(Integer goodsType) {
        if (goodsType == 1) {
            this.goodsTypeDesc = "进口";
        } else if (goodsType == 2) {
            this.goodsTypeDesc = "出口";
        }
    }

    public String getStatusDesc() {
        if (!StringUtil.isNullOrEmpty(this.status)) {
            return OrderStatusEnum.getDesc(this.status);
        }
        return "";
    }

    public void setSubUnitCode(String subUnitCode) {
        this.subUnitCode = subUnitCode;
        this.defaultUnitCode = subUnitCode;
    }
}
