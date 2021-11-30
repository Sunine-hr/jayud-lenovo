package com.jayud.tms.model.vo;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.SubOrderSignEnum;
import com.jayud.common.enums.UserTypeEnum;
import com.jayud.common.utils.FileView;
import com.jayud.common.utils.StringUtils;
import com.jayud.tms.model.po.OrderTakeAdr;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.xmlbeans.UserType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class OrderTransportVO {

    @ApiModelProperty(value = "子订单ID")
    private Long id;

    @ApiModelProperty(value = "主订单ID")
    private Long mainOrderId;

    @ApiModelProperty(value = "主订单")
    private String mainOrderNo;

    @ApiModelProperty(value = "已选中得服务")
    private String selectedServer;

    @ApiModelProperty(value = "第三方单号")
    private String thirdPartyOrderNo;

    @ApiModelProperty(value = "业务类型")
    private String bizCode;

    @ApiModelProperty(value = "子订单号")
    private String orderNo;

    @ApiModelProperty(value = "通关口岸code")
    private String portCode;

    @ApiModelProperty(value = "通关口岸")
    private String portName;

    @ApiModelProperty(value = "货物流向(1进口 2出口)")
    private Integer goodsType;

    @ApiModelProperty(value = "货物流向描述")
    private Integer goodsTypeDesc;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "作业类型")
    private String classCode;

    @ApiModelProperty(value = "作业类型描述")
    private String classCodeDesc;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "客户名称CODE")
    private String customerCode;

    @ApiModelProperty(value = "六联单号")
    private String encode;

    @ApiModelProperty(value = "车型")
    private String vehicleType;

    @ApiModelProperty(value = "车型尺寸")
    private String vehicleSize;

    @ApiModelProperty(value = "柜号")
    private String cntrNo;

    @ApiModelProperty(value = "是否有费用详情")
    private Boolean cost;

    @ApiModelProperty(value = "中港运输状态,用于标识驳回可编辑")
    private String subTmsStatus;

    @ApiModelProperty(value = "中港运输状态,用于标识驳回可编辑")
    private String subTmsStatusDesc;

    @ApiModelProperty(value = "是否需要录入费用")
    private Boolean needInputCost;

    @ApiModelProperty(value = "状态描述")
    private String statusDesc;

    @ApiModelProperty(value = "法人主体")
    private String legalName;

    @ApiModelProperty(value = "结算单位")
    private String unitCode;

    @ApiModelProperty(value = "子订单法人主体")
    private String subLegalName;

    @ApiModelProperty(value = "子订单结算单位")
    private String subUnitCode;

    @ApiModelProperty(value = "提货文件上传附件地址,前台忽略")
    private String file1;

    @ApiModelProperty(value = "提货文件上传附件地址名称,前台忽略")
    private String fileName1;

    @ApiModelProperty(value = "送货文件上传附件地址,前台忽略")
    private String file2;

    @ApiModelProperty(value = "送货文件上传附件地址名称,前台忽略")
    private String fileName2;

    @ApiModelProperty(value = "提货文件上传附件地址数组集合")
    private List<FileView> takeFiles1 = new ArrayList<>();

    @ApiModelProperty(value = "送货文件上传附件地址数组集合")
    private List<FileView> takeFiles2 = new ArrayList<>();

    @ApiModelProperty(value = "过磅数KG")
    private Double carWeighNum;

    //货物信息
    @ApiModelProperty(value = "货物描述")
    private String goodsDesc;

    @ApiModelProperty(value = "板数")
    private Integer plateAmount;

    @ApiModelProperty(value = "件数")
    private Integer pieceAmount;

    @ApiModelProperty(value = "重量")
    private Double weight;

    @ApiModelProperty(value = "总重量")
    private Double totalWeight;

    @ApiModelProperty(value = "提货时间,多个逗号拼接")
    private String takeTimeStr;

    //提供货信息
//    @ApiModelProperty(value = "省")//提货
//    private String stateName1;
//
//    @ApiModelProperty(value = "市")
//    private String cityName1;
//
//    @ApiModelProperty(value = "详细地址")
//    private String address1;

    @ApiModelProperty(value = "提货完整地址")
    private String entireAddress1;

//    @ApiModelProperty(value = "省")//送货
//    private String stateName2;
//
//    @ApiModelProperty(value = "市")
//    private String cityName2;
//
//    @ApiModelProperty(value = "详细地址")
//    private String address2;

    @ApiModelProperty(value = "送货完整地址")
    private String entireAddress2;

    @ApiModelProperty(value = "创建人")
    private String createdUser;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private String createdTimeStr;

    @ApiModelProperty(value = "商品信息")
    private String goodsInfo;

    @ApiModelProperty(value = "中转仓库id")
    private Long warehouseInfoId;

    @ApiModelProperty(value = "中转仓库")
    private String warehouseName;

    @ApiModelProperty(value = "供应商代码")
    private String defaultSupplierCode;

    @ApiModelProperty(value = "结算单位code")
    private String defaultUnitCode;

    @ApiModelProperty(value = "是否费用提交审核")
    private Boolean isCostSubmitted;

    @ApiModelProperty(value = "车辆id")
    private Long vehicleId;

    @ApiModelProperty(value = "司机id")
    private Long driverInfoId;

    @ApiModelProperty(value = "车牌")
    private String plateNumber;

    @ApiModelProperty(value = "主订单备注")
    private String mainOrderRemarks;

    @ApiModelProperty(value = "应收费用状态")
    private String receivableCostStatus;

    @ApiModelProperty(value = "应付费用状态")
    private String paymentCostStatus;

    @ApiModelProperty(value = "标识")
    private String mark = SubOrderSignEnum.ZGYS.getSignOne();

    @ApiModelProperty(value = "是否虚拟仓库")
    @JsonIgnore
    private Boolean isVirtual;

    @ApiModelProperty(value = "中转仓库详细地址")
    @JsonIgnore
    private String warehouseAddr;

    @ApiModelProperty(value = "指派供应商吨位")
    private String supplierVehicleSize;

    @ApiModelProperty(value = "操作部门id")
    private Long departmentId;

    @ApiModelProperty(value = "操作部门")
    private String department;

    @ApiModelProperty(value = "对接GPS所需要的key值")
    private String appKey;

    @ApiModelProperty(value = "对接GPS公用路径前缀")
    private String gpsAddress;

    @ApiModelProperty(value = "签收时间")
    private String signTime;

    /**
     * 组装商品信息
     */
//    public void assemblyGoodsInfo(List<OrderTakeAdr> orderTakeAdrs) {
//        StringBuilder sb = new StringBuilder();
//        Integer pieceAmount = 0;
//        Integer plateAmount = 0;
//
//        for (OrderTakeAdr orderTakeAdr : orderTakeAdrs) {
//            if (this.orderNo.equals(orderTakeAdr.getOrderNo())) {
//                sb.append(orderTakeAdr.getGoodsDesc())
//                        .append("/").append(orderTakeAdr.getPlateAmount() == null ? 0 : orderTakeAdr.getPlateAmount()).append("板")
//                        .append("/").append(orderTakeAdr.getPieceAmount()).append("件")
//                        .append("/").append("重量").append(orderTakeAdr.getWeight()).append("KG")
//                        .append(",");
//            }
//
//        }
//        this.goodsInfo = sb.toString();
//    }
    public String getStatusDesc() {
        return OrderStatusEnum.getDesc(this.status);
    }


    public void setGoodsType(Integer goodsType) {
        this.goodsType = goodsType;
    }

    public String getGoodsTypeDesc() {
        if (goodsType != null) {
            if (this.goodsType == 1) {
                return "进口";
            } else if (this.goodsType == 2) {
                return "出口";
            }
        }
        return "";
    }

    public void setSubUnitCode(String subUnitCode) {
        this.subUnitCode = subUnitCode;
        this.defaultUnitCode = subUnitCode;
    }

    public void setSubTmsStatus(String subTmsStatus) {
        this.subTmsStatus = subTmsStatus;
        this.subTmsStatusDesc = OrderStatusEnum.getDesc(subTmsStatus);
    }

    public void assemblyTakeFiles(List<OrderTakeAdr> takeAdrsList, String prePath) {
        if (CollectionUtil.isEmpty(takeAdrsList)) {
            return;
        }
        for (OrderTakeAdr orderTakeAdr : takeAdrsList) {
            if (this.orderNo.equals(orderTakeAdr.getOrderNo())) {
                if (1 == orderTakeAdr.getOprType()) {
                    takeFiles1 = StringUtils.getFileViews(orderTakeAdr.getFile(), orderTakeAdr.getFileName(), prePath);
                }
                if (2 == orderTakeAdr.getOprType()) {
                    takeFiles2 = StringUtils.getFileViews(orderTakeAdr.getFile(), orderTakeAdr.getFileName(), prePath);
                }
            }
        }
    }

    public void assemblyTakeAdrInfos(List<OrderTakeAdrInfoVO> takeAdrsList, String prePath) {
        if (CollectionUtil.isEmpty(takeAdrsList)) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder goodsDesc = new StringBuilder();
        StringBuilder pickUpAddr = new StringBuilder();
        StringBuilder deliveryAddr = new StringBuilder();
        StringBuilder takeTimeStr = new StringBuilder();
        takeFiles1 = new ArrayList<>();
        takeFiles2 = new ArrayList<>();
        Integer pieceAmount = 0;
        Integer plateAmount = 0;
        Double totalWeight = 0.0;

        for (OrderTakeAdrInfoVO orderTakeAdr : takeAdrsList) {
            if (this.orderNo.equals(orderTakeAdr.getOrderNo())) {
                if (1 == orderTakeAdr.getOprType()) {
                    //提货时间
//                    if (this.takeTimeStr==null) {
//                        takeTimeStr.append(orderTakeAdr.getTakeTimeStr()).append(",");
//
//                    }


                    takeFiles1.addAll(StringUtils.getFileViews(orderTakeAdr.getFile(), orderTakeAdr.getFileName(), prePath));
                    //商品名称
                    sb.append(orderTakeAdr.getGoodsDesc())
                            .append("/").append(orderTakeAdr.getPlateAmount() == null ? 0 : orderTakeAdr.getPlateAmount()).append("板")
                            .append("/").append(orderTakeAdr.getPieceAmount()).append("件")
                            .append("/").append("重量").append(orderTakeAdr.getWeight()).append("KG")
                            .append(",");


                    //提货地址
                    pickUpAddr.append(orderTakeAdr.getAddress()).append(",");
                    pieceAmount += orderTakeAdr.getPieceAmount() == null ? 0 : orderTakeAdr.getPieceAmount();
                    plateAmount += orderTakeAdr.getPlateAmount() == null ? 0 : orderTakeAdr.getPlateAmount();
                    totalWeight += orderTakeAdr.getWeight() == null ? 0 : orderTakeAdr.getWeight();
                }
                if (2 == orderTakeAdr.getOprType()) {
                    takeFiles2.addAll(StringUtils.getFileViews(orderTakeAdr.getFile(), orderTakeAdr.getFileName(), prePath));
                    //送货地址
                    deliveryAddr.append(orderTakeAdr.getAddress()).append(",");
                }
            }
        }
        this.goodsInfo = sb.toString();
        this.pieceAmount = pieceAmount;
        this.plateAmount = plateAmount;
        this.totalWeight = totalWeight;
        this.entireAddress1 = pickUpAddr.toString();
        this.entireAddress2 = deliveryAddr.toString();
//        this.takeTimeStr = takeTimeStr.toString();
    }

    public void assemblyCostStatus(Map<String, Object> costStatus) {
        if (CollectionUtil.isEmpty(costStatus)) return;

        Map<String, Object> receivableCostStatus = (Map<String, Object>) costStatus.get("receivableCostStatus");
        Map<String, Object> paymentCostStatus = (Map<String, Object>) costStatus.get("paymentCostStatus");

        String receivableStatusDesc = MapUtil.getStr(receivableCostStatus, orderNo);
        String paymentStatusDesc = MapUtil.getStr(paymentCostStatus, orderNo);

        if (!StringUtils.isEmpty(receivableStatusDesc)) {
            String[] split = receivableStatusDesc.split("-", 2);
            this.receivableCostStatus = split[0];
        } else {
            this.receivableCostStatus = "未录入";
        }

        if (!StringUtils.isEmpty(paymentStatusDesc)) {
            String[] split = paymentStatusDesc.split("-", 2);
            this.paymentCostStatus = split[0];
        } else {
            this.paymentCostStatus = "未录入";
        }
    }

    public void doFilterData(String accountType) {
        switch (UserTypeEnum.getEnum(accountType)) {
            case EMPLOYEE_TYPE:
                break;
            case CUSTOMER_TYPE:
                break;
            case SUPPLIER_TYPE:
                this.customerName = this.subLegalName;
                this.receivableCostStatus = null;
//                this.subLegalName = null;
                this.legalName = null;
                this.vehicleSize = this.supplierVehicleSize;
                //送货地址处理
                this.shippingAddressHandle();
                break;
        }
    }

//    public static void main(String[] args) {
//        String str="你好-5";
//        System.out.println(str.split("-",2)[0]+" " +str.split("-",2)[1]);
//    }

    /**
     * 送货地址处理
     * 多个地址并且不是虚拟仓,展示中转仓地址
     */
    private void shippingAddressHandle() {
        if (this.entireAddress2.length() > 1) {
            if (this.isVirtual == null || !this.isVirtual) {
                this.entireAddress2 = this.warehouseAddr;
            }
        }

    }

    /**
     * 组装中转仓库地址
     *
     * @param warehouseMap
     */
    public void assemblyWarehouse(Map<Long, Map<String, Object>> warehouseMap) {
        if (CollectionUtil.isEmpty(warehouseMap)) {
            return;
        }
        Object tmp = warehouseMap.get(this.warehouseInfoId);
        if (tmp == null) {
            return;
        }
        JSONObject jsonObject = new JSONObject(tmp);
        this.warehouseName = jsonObject.getStr("warehouseName");
        this.isVirtual = jsonObject.getBool("isVirtual");
        this.warehouseAddr = jsonObject.getStr("address");
    }
}
