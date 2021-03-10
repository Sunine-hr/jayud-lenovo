package com.jayud.Inlandtransport.model.vo;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.enums.OrderAddressEnum;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ProcessStatusEnum;
import com.jayud.common.utils.StringUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 内陆订单
 * </p>
 *
 * @author LDR
 * @since 2021-03-01
 */
@Data
@Slf4j
public class OrderInlandTransportFormVO extends Model<OrderInlandTransportFormVO> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "内陆订单主键")
    private Long id;

    @ApiModelProperty(value = "主订单编号")
    private String mainOrderNo;

    @ApiModelProperty(value = "主订单id")
    private String mainOrderId;

    @ApiModelProperty(value = "订单编号", required = true)
    private String orderNo;

    @ApiModelProperty(value = "客户名称", required = true)
    private String customerName;

//    @ApiModelProperty(value = "操作主体", required = true)
//    private String legalName;

    @ApiModelProperty(value = "操作主体",required = true)
    private String subLegalName;

    @ApiModelProperty(value = "车型", required = true)
    private String vehicleSize;

    @ApiModelProperty(value = "流程状态", required = true)
    private String processStatusDesc;

//    @ApiModelProperty(value = "订单状态",required = true)
//    private String inlandStatusDesc;

    @ApiModelProperty(value = "状态", required = true)
    private String statusDesc;

    @ApiModelProperty(value = "货物信息", required = true)
    private String goodsInfo;

    @ApiModelProperty("提货地址")
    private String pickUpAddress;

    @ApiModelProperty("送货地址")
    private String orderDeliveryAddress;

    @ApiModelProperty(value = "创建人", required = true)
    private String createUser;

    @ApiModelProperty(value = "创建时间", required = true)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "流程状态")
    private Integer processStatus;

    @ApiModelProperty(value = "状态(NL_0待接单,NL_1内陆接单,NL_1_1内陆接单驳回,NL_2内陆派车,NL_2_1内陆派车驳回,NL_3派车审核, NL_3_1派车审核不通过,NL_3_2派车审核驳回,L_4确认派车,NL_4_1确认派车驳回,NL_5车辆提货,NL_5_1车辆提货驳回,NL_6货物签收)")
    private String status;

//    @ApiModelProperty(value = "车型(1吨车 2柜车)")
//    private Integer vehicleType;

    @ApiModelProperty(value = "结算单位CODE")
    private String unitCode;

    @ApiModelProperty(value = "操作主体id(法人主体ID)")
    private Long legalEntityId;

    @ApiModelProperty(value = "是否需要录入费用")
    private Boolean needInputCost;

//    @ApiModelProperty(value = "接单人(登录用户名)")
//    private String orderTaker;

//    @ApiModelProperty(value = "接单日期")
//    private LocalDateTime receivingOrdersDate;


    @ApiModelProperty(value = "客户代码")
    private String customerCode;

    @ApiModelProperty(value = "订单业务人员名称")
    private String bizUname;

    @ApiModelProperty(value = "对应业务类型")
    private String bizCode;

    @ApiModelProperty(value = "订单类别")
    private String classCode;

    @ApiModelProperty(value = "供应商id")
    private Long supplierId;

    @ApiModelProperty(value = "供应商代码")
    private String defaultSupplierCode;

    @ApiModelProperty(value = "费用录用默认结算单位")
    private String defaultUnitCode;

    @ApiModelProperty(value = "结算单位code")
    private String subUnitCode;

    @ApiModelProperty(value = "是否录用费用")
    private Boolean cost;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTimeStr;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        this.createdTimeStr = createTime;
    }

    /**
     * 组装商品信息
     */
    public void assemblyGoodsInfo(List<GoodsVO> goodsList) {
        StringBuilder sb = new StringBuilder();

        for (GoodsVO goods : goodsList) {
            if (this.id.equals(goods.getBusinessId())) {
                sb.append(goods.getName())
                        .append("/").append(goods.getPlateAmount() == null ? 0 : goods.getPlateAmount())
                        .append(StringUtils.isEmpty(goods.getPlateUnit()) ? "板" : goods.getPlateUnit())
                        .append("/").append(goods.getBulkCargoAmount())
                        .append(StringUtils.isEmpty(goods.getBulkCargoUnit()) ? "件" : goods.getBulkCargoUnit())
                        .append("/").append("重量").append(goods.getTotalWeight()).append("KG")
                        .append(",");
            }
        }
        this.goodsInfo = sb.substring(0, sb.length() - 1);
    }

    /**
     * 组装地址信息
     *
     * @param orderAddressList
     */
    public void assemblyAddressInfo(List<OrderAddressVO> orderAddressList) {
        StringBuilder pickUpAddressSb = new StringBuilder();
        StringBuilder orderDeliveryAddressSb = new StringBuilder();
        orderAddressList.forEach(e -> {
            if (OrderAddressEnum.PICK_UP.getCode().equals(e.getType())) {
                pickUpAddressSb.append(e.getAddress()).append(",");
            }
            if (OrderAddressEnum.DELIVERY.getCode().equals(e.getType())) {
                orderDeliveryAddressSb.append(e.getAddress()).append(",");
            }
        });
        this.pickUpAddress = pickUpAddressSb.toString();
        this.orderDeliveryAddress = orderDeliveryAddressSb.toString();
    }

    /**
     * @param mainOrderObjs 远程客户对象集合
     */
    public void assemblyMainOrderData(Object mainOrderObjs) {
        if (mainOrderObjs == null) {
            return;
        }
        JSONArray mainOrders = new JSONArray(JSON.toJSONString(mainOrderObjs));
        for (int i = 0; i < mainOrders.size(); i++) {
            JSONObject json = mainOrders.getJSONObject(i);
            if (this.mainOrderNo.equals(json.getStr("orderNo"))) { //主订单配对
                this.customerName = json.getStr("customerName");
                this.customerCode = json.getStr("customerCode");
                this.mainOrderId = json.getStr("id");
                this.bizUname = json.getStr("bizUname");
                this.bizCode = json.getStr("bizCode");
                this.classCode = json.getStr("classCode");
                break;
            }
        }

    }

    /**
     * 组装供应商数据
     */
    public void assemblySupplierInfo(JSONArray supplierInfo) {
        if (supplierInfo == null) {
            return;
        }
        for (int i = 0; i < supplierInfo.size(); i++) {
            JSONObject json = supplierInfo.getJSONObject(i);
            if (this.supplierId != null && this.supplierId.equals(json.getLong("id"))) { //供应商配对
                this.defaultSupplierCode = json.getStr("supplierCode");
                break;
            }
        }
    }

    /**
     * 组装法人主体
     *
     * @param legalEntityResult
     */
//    public void assemblyLegalEntity(ApiResult legalEntityResult) {
//        if (legalEntityResult == null) {
//            return;
//        }
//        if (legalEntityResult.getCode() != HttpStatus.SC_OK) {
//            log.warn("请求法人主体信息失败");
//            return;
//        }
//        JSONArray legalEntitys = new JSONArray(legalEntityResult.getData());
//        for (int i = 0; i < legalEntitys.size(); i++) {
//            JSONObject json = legalEntitys.getJSONObject(i);
//            if (this.legalEntityId.equals(json.getLong("id"))) { //法人主体配对
//                this.subLegalName = json.getStr("legalName");
//                break;
//            }
//        }
//    }
    public void setStatus(String status) {
        this.status = status;
        this.statusDesc = OrderStatusEnum.getDesc(status);
    }


    public void setProcessStatus(Integer processStatus) {
        this.processStatus = processStatus;
        this.processStatusDesc = ProcessStatusEnum.getDesc(processStatus);
    }

    public void setSubUnitCode(String subUnitCode) {
        this.subUnitCode = subUnitCode;
        this.defaultUnitCode = subUnitCode;
    }



}
