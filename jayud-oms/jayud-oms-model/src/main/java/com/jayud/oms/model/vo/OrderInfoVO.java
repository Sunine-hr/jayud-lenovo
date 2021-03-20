package com.jayud.oms.model.vo;

import com.jayud.common.constant.CommonConstant;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.utils.StringUtils;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class OrderInfoVO {

    @ApiModelProperty(value = "主订单ID")
    private Integer id;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "业务类型")
    private String bizCode;

    @ApiModelProperty(value = "作业类型")
    private String classCodeDesc;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "结算单位")
    private String unitAccount;

    @ApiModelProperty(value = "合同号")
    private String contractNo;

    @ApiModelProperty(value = "客户参考号")
    private String referenceNo;

    @ApiModelProperty(value = "业务员")
    private String bizUname;

    @ApiModelProperty(value = "是否有费用详情")
    private boolean isCost;

    @ApiModelProperty(value = "主订单备注")
    private String remarks;


//    @ApiModelProperty(value = "进出口类型")
//    private String goodsType;
//
//    @ApiModelProperty(value = "进出口类型描述")
//    private String goodsTypeDesc;

//    @ApiModelProperty(value = "通关口岸")
//    private String portName;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "状态描述")
    private String statusDesc;



    @ApiModelProperty(value = "作业类型CODE")
    private String classCode;

    @ApiModelProperty(value = "客户名称CODE")
    private String customerCode;

    @ApiModelProperty(value = "法人主体")
    private String legalName;

    @ApiModelProperty(value = "货物信息")
    private String goodsInfo;

    @ApiModelProperty(value = "提货地址")
    private String takeAddress;

    @ApiModelProperty(value = "送货地址")
    private String giveAddress;

    @ApiModelProperty(value = "创建人")
    private String createdUser;

    @ApiModelProperty(value = "创建时间")
    private String createdTimeStr;

//    @ApiModelProperty(value = "报关状态,用于标识驳回可编辑")
//    private String subCustomsStatus;
//
//    @ApiModelProperty(value = "中港运输状态,用于标识驳回可编辑")
//    private String subTmsStatus;

//    @ApiModelProperty(value = "报关状态描述,用于标识驳回可编辑")
//    private String subCustomsDesc;

//    @ApiModelProperty(value = "中港运输状态描述,用于标识驳回可编辑")
//    private String subTmsDesc;


//    @ApiModelProperty(value = "空运状态,用于标识驳回可编辑")
//    private String subAirStatus;
//
//
//    @ApiModelProperty(value = "内陆状态,用于标识驳回可编辑")
//    private String subInlandStatus;

//    @ApiModelProperty(value = "空运状态描述,用于标识驳回可编辑")
//    private String subAirDesc;

//    @ApiModelProperty(value = "备注")
//    private String remarks;

    @ApiModelProperty(value = "是否需要录入费用")
    private Boolean needInputCost;

    @ApiModelProperty(value = "已选中得服务")
    private String selectedServer;

    @ApiModelProperty(value = "驳回原因,目前只要求显示报关驳回的")
    private String rejectComment;

    @ApiModelProperty(value = "结算单位")
    private String unitCode;

    @ApiModelProperty(value = "费用录用默认结算单位")
    private String defaultUnitCode;

//    @ApiModelProperty(value = "子订单状态描述")
//    private String subOrderStatusDesc;
//
//    @ApiModelProperty(value = "子订单状态描述增加换行")
//    private String subOrderStatusDescOne;




//    @ApiModelProperty(value = "中港订单号")
//    private String tmsOrderNo;
//
//    @ApiModelProperty(value = "报关订单号")
//    private String customsOrderNo;
//
//    @ApiModelProperty(value = "空运订单号")
//    private String airOrderNo;

    public String getStatusDesc() {
        return OrderStatusEnum.getDesc(this.status);
    }

//    public String getGoodsTypeDesc() {
//        if (CommonConstant.VALUE_1.equals(this.goodsType)) {
//            goodsTypeDesc = CommonConstant.GOODS_TYPE_DESC_1;
//        } else if (CommonConstant.VALUE_2.equals(this.goodsType)) {
//            goodsTypeDesc = CommonConstant.GOODS_TYPE_DESC_2;
//        }
//        return goodsTypeDesc;
//    }

//    public String getSubCustomsDesc() {
//        if (!StringUtil.isNullOrEmpty(this.subCustomsStatus)) {
//            String desc = "";
//            StringBuilder sb = new StringBuilder();
//            String[] strs = this.subCustomsStatus.split(",");
//            for (String str : strs) {
//                sb.append(OrderStatusEnum.getDesc(this.subCustomsStatus)).append(",");
//            }
//            if (!"".equals(String.valueOf(sb))) {
//                desc = sb.substring(0, sb.length() - 1);
//            }
//            return desc;
//        }
//        return "";
//    }

//    public String getSubTmsDesc() {
//        return OrderStatusEnum.getDesc(this.subTmsStatus);
////    }

//    public void setSubAirStatus(String subAirStatus) {
//        this.subAirStatus = subAirStatus;
////        this.subAirDesc=OrderStatusEnum.getDesc(this.subAirStatus);
//    }


//    public String getSubOrderStatusDesc() {
//        StringBuffer sb = new StringBuffer();
//        String[] descs = new String[]{
//                this.tmsOrderNo + "-" + OrderStatusEnum.getDesc(this.subTmsStatus),
//                this.customsOrderNo + "-" + this.getSubCustomsDesc(),
//                this.airOrderNo + "-" + OrderStatusEnum.getDesc(this.subAirStatus)};
//
//        for (String desc : descs) {
//            if (!StringUtils.isEmpty(desc) && desc.split("-").length > 1) {
//                sb.append(desc).append(",");
//            }
//        }
//        return sb.length() == 0 ? "" : sb.substring(0, sb.length() - 1);
//    }

//    public void setSubOrderStatusDesc(String subOrderStatusDesc) {
//        this.subOrderStatusDesc = subOrderStatusDesc;
//        this.subOrderStatusDescOne = subOrderStatusDesc.replaceAll(",", "<br/>");
//    }


//    public String getRemarks() {
//        StringBuffer sb = new StringBuffer();
//        String[] descs = new String[]{OrderStatusEnum.getDesc(this.subTmsStatus),
//                this.getSubCustomsDesc(), OrderStatusEnum.getDesc(this.subAirStatus)};
//        for (String desc : descs) {
//            if (!StringUtils.isEmpty(desc)) {
//                sb.append(desc).append(",");
//            }
//        }
//        return sb.length() == 0 ? "" : sb.substring(0, sb.length() - 1);
//    }


    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
        this.defaultUnitCode = unitCode;
    }
}
