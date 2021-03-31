package com.jayud.tms.model.vo.statistical;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.tms.model.po.OrderTakeAdr;
import com.jayud.tms.model.vo.DriverOrderTakeAdrVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Data
public class TVOrderTransportVO {

    @ApiModelProperty(value = "子订单ID")
    @JsonIgnore
    private Long id;

    @ApiModelProperty(value = "主订单ID")
    @JsonIgnore
    private Long mainOrderId;

    @ApiModelProperty(value = "主订单")
    private String mainOrderNo;

//    @ApiModelProperty(value = "已选中得服务")
//    private String selectedServer;

//    @ApiModelProperty(value = "业务类型")
//    private String bizCode;

    @ApiModelProperty(value = "子订单号")
    private String orderNo;

//    @ApiModelProperty(value = "通关口岸")
//    private String portName;

//    @ApiModelProperty(value = "货物流向(1进口 2出口)")
//    private Integer goodsType;

//    @ApiModelProperty(value = "货物流向描述")
//    private Integer goodsTypeDesc;

    @ApiModelProperty(value = "状态")
    @JsonIgnore
    private String status;

//    @ApiModelProperty(value = "作业类型")
//    private String classCode;

    @ApiModelProperty(value = "作业类型描述")
    @JsonIgnore
    private String classCodeDesc;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

//    @ApiModelProperty(value = "客户名称CODE")
//    private String customerCode;


//    @ApiModelProperty(value = "车型")
//    private String vehicleType;
//
//    @ApiModelProperty(value = "车型尺寸")
//    private String vehicleSize;
//
//    @ApiModelProperty(value = "柜号")
//    private String cntrNo;


    @ApiModelProperty(value = "中港运输状态")
    private String statusDesc;

    @ApiModelProperty(value = "法人主体")
    @JsonIgnore
    private String legalName;

//    @ApiModelProperty(value = "结算单位")
//    private String unitCode;

//    @ApiModelProperty(value = "子订单法人主体")
//    private String subLegalName;
//
//    @ApiModelProperty(value = "子订单结算单位")
//    private String subUnitCode;


//    @ApiModelProperty(value = "过磅数KG")
//    private Double carWeighNum;

    //货物信息
//    @ApiModelProperty(value = "货物描述")
//    private String goodsDesc;

//    @ApiModelProperty(value = "板数")
//    private String plateAmount;
//
//    @ApiModelProperty(value = "件数")
//    private String pieceAmount;
//
//    @ApiModelProperty(value = "重量")
//    private String weight;

    @ApiModelProperty(value = "提货时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime takeTime;

    @ApiModelProperty(value = "送货时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime deliveryTime;

    @ApiModelProperty(value = "提货地址")
    private String pickUpAddress;

    @ApiModelProperty(value = "送货地址")
    private String deliveryAddress;

//    @ApiModelProperty(value = "创建人")
//    private String createdUser;
//
//    @ApiModelProperty(value = "创建时间")
//    private String createdTimeStr;

//    @ApiModelProperty(value = "商品信息")
//    private String goodsInfo;

    @ApiModelProperty(value = "中转仓库")
    @JsonIgnore
    private String warehouseName;

    @ApiModelProperty(value = "是否虚拟仓")
    @JsonIgnore
    private Boolean isVirtualWarehouse;

    @ApiModelProperty(value = "中转仓库id")
    @JsonIgnore
    private Long warehouseInfoId;

    @ApiModelProperty(value = "车牌")
    private String plateNumber;

    @ApiModelProperty(value = "车牌Id")
    @JsonIgnore
    private Long vehicleId;

    @ApiModelProperty(value = "司机Id")
    @JsonIgnore
    private Long driverInfoId;

    @ApiModelProperty(value = "司机姓名")
    private String driverName;

    @ApiModelProperty(value = "节点数据")
    private List<ProcessNode> processNodes = new ArrayList<>();

//    @ApiModelProperty(value = "是否有费用详情")
//    private boolean isCost;


    /**
     * 组装商品信息
     */
//    public void assemblyGoodsInfo(List<OrderTakeAdr> orderTakeAdrs) {
//        StringBuilder sb = new StringBuilder();
//
//        for (OrderTakeAdr orderTakeAdr : orderTakeAdrs) {
//            if (this.orderNo.equals(orderTakeAdr.getOrderNo())) {
//                sb.append(orderTakeAdr.getGoodsDesc())
//                        .append("/").append(orderTakeAdr.getPlateAmount() == null ? 0 : orderTakeAdr.getPlateAmount()).append("板")
//                        .append("/").append(orderTakeAdr.getPieceAmount()).append("件")
//                        .append("/").append("重量").append(orderTakeAdr.getWeight()).append("KG")
//                        .append(",");
//            }
//        }
//        this.goodsInfo = sb.substring(0, sb.length() - 1);
//    }
    public void setStatus(String status) {
        this.status = status;
        OrderStatusEnum enums = OrderStatusEnum.getEnums(status);
        switch (enums) {
            case TMS_T_0:
            case TMS_T_1:
            case TMS_T_2:
            case TMS_T_3:
            case TMS_T_4:
                this.statusDesc = "接单";
                break;
            case TMS_T_5:
                this.statusDesc = "提货";
                break;
            case TMS_T_6:
            case TMS_T_7:
            case TMS_T_8:
            case TMS_T_9:
            case TMS_T_9_1:
            case TMS_T_9_2:
            case TMS_T_10:
            case TMS_T_11:
            case TMS_T_12:
            case TMS_T_13:
            case TMS_T_14:
                this.statusDesc = "运输中";
                break;
            case TMS_T_15:
                this.statusDesc = "完成";
        }
    }


    public void assemblyTakeAddr(List<DriverOrderTakeAdrVO> takeAdrsList) {
        if (CollectionUtil.isEmpty(takeAdrsList)) {
            return;
        }

        StringBuilder pickUpAddress = new StringBuilder();
        StringBuilder deliveryAddress = new StringBuilder();

        //实体仓库:送货地址取中转仓
        if (!isVirtualWarehouse) {
            deliveryAddress.append(this.warehouseName, 0, Math.min(this.warehouseName.length(), 6));
        }

        for (DriverOrderTakeAdrVO orderTakeAdr : takeAdrsList) {
            if (this.orderNo.equals(orderTakeAdr.getOrderNo())) {
                String address = orderTakeAdr.getAddress() == null ? "" : orderTakeAdr.getAddress();
                if (1 == orderTakeAdr.getOprType()) { //提货地址
                    if (pickUpAddress.length() == 0) {
                        pickUpAddress.append(orderTakeAdr.getAddress(), 0, Math.min(address.length(), 6));
                        this.takeTime = orderTakeAdr.getTakeTime();
                    }
                } else {
                    if (this.deliveryTime == null) {
                        this.deliveryTime = orderTakeAdr.getTakeTime();
                    }
                    //虚拟仓:获取送货地址第一个
                    if (deliveryAddress.length() > 0) {
                        continue;
                    }
                    deliveryAddress.append(address, 0, Math.min(address.length(), 6));
                }
            }
        }
        this.pickUpAddress = pickUpAddress.toString();
        this.deliveryAddress = deliveryAddress.toString();
    }


    public void assemblyMainOrderData(Object data) {
        if (data == null) {
            return;
        }
        JSONArray mainOrders = new JSONArray(data);
        for (int i = 0; i < mainOrders.size(); i++) {
            JSONObject json = mainOrders.getJSONObject(i);
            if (this.mainOrderNo.equals(json.getStr("orderNo"))) { //主订单配对
                this.customerName = json.getStr("customerName", "");
                Integer index = -1;
                if (customerName.contains("有限公司")) {
                    index = customerName.indexOf("有限公司");
                } else {
                    index = customerName.indexOf("LIMITED");
                }
                this.customerName = this.customerName.substring(0, index > 0 ? index : customerName.length());
                this.customerName = this.customerName.substring(0, Math.min(this.customerName.length(), 12));

                this.mainOrderId = json.getLong("id");
//                this.bizCode = json.getStr("bizCode");
//                this.classCode = json.getStr("classCode");
                break;
            }
        }
    }

    public void assemblyVehicleInfo(Object data) {
        if (data == null) {
            return;
        }
        JSONArray arrays = new JSONArray(data);
        for (int i = 0; i < arrays.size(); i++) {
            JSONObject json = arrays.getJSONObject(i);
            if (this.vehicleId != null && this.vehicleId.equals(json.getLong("id"))) { //车辆配对
                this.plateNumber = json.getStr("plateNumber");
                break;
            }
        }
    }

    public void assemblyWarehouseInfo(Object data) {
        if (data == null) {
            return;
        }
        JSONArray arrays = new JSONArray(data);
        for (int i = 0; i < arrays.size(); i++) {
            JSONObject json = arrays.getJSONObject(i);
            if (this.warehouseInfoId.equals(json.getLong("id"))) {
                this.warehouseName = json.getStr("warehouseName");
                this.isVirtualWarehouse = json.getBool("isVirtual");
                break;
            }
        }
    }

    public void assemblyDriverInfo(Object data) {
        if (data == null) {
            return;
        }
        JSONArray arrays = new JSONArray(data);
        for (int i = 0; i < arrays.size(); i++) {
            JSONObject json = arrays.getJSONObject(i);
            if (this.driverInfoId != null && this.driverInfoId.equals(json.getLong("id"))) {
                this.driverName = json.getStr("name");
                break;
            }
        }
    }


    public void assemblyProcessNode(List<ProcessNode> processNodes) {

        this.processNodes = processNodes;

    }
}
