package com.jayud.tms.model.vo;

import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.utils.FileView;
import com.jayud.tms.model.po.OrderTakeAdr;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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

    @ApiModelProperty(value = "业务类型")
    private String bizCode;

    @ApiModelProperty(value = "子订单号")
    private String orderNo;

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
    private boolean isCost;

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
    private String plateAmount;

    @ApiModelProperty(value = "件数")
    private String pieceAmount;

    @ApiModelProperty(value = "重量")
    private String weight;

    @ApiModelProperty(value = "总重量")
    private String totalWeight;

    @ApiModelProperty(value = "提货时间,多个逗号拼接")
    private String takeTimeStr;

    //提供货信息
    @ApiModelProperty(value = "省")//提货
    private String stateName1;

    @ApiModelProperty(value = "市")
    private String cityName1;

    @ApiModelProperty(value = "详细地址")
    private String address1;

    @ApiModelProperty(value = "提货完整地址")
    private String entireAddress1;

    @ApiModelProperty(value = "省")//送货
    private String stateName2;

    @ApiModelProperty(value = "市")
    private String cityName2;

    @ApiModelProperty(value = "详细地址")
    private String address2;

    @ApiModelProperty(value = "送货完整地址")
    private String entireAddress2;

    @ApiModelProperty(value = "创建人")
    private String createdUser;

    @ApiModelProperty(value = "创建时间")
    private String createdTimeStr;

    @ApiModelProperty(value = "商品信息")
    private String goodsInfo;

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

    public String getEntireAddress1() {
        String stateName1 = this.stateName1;
        String cityName1 = this.cityName1;
        String address1 = this.address1;
        if (StringUtil.isNullOrEmpty(this.stateName1)) {
            stateName1 = "";
        }
        if (StringUtil.isNullOrEmpty(this.cityName1)) {
            cityName1 = "";
        }
        if (StringUtil.isNullOrEmpty(this.address1)) {
            address1 = "";
        }
        return this.entireAddress1 = stateName1 + cityName1 + address1;
    }

    public String getEntireAddress2() {
        String stateName2 = this.stateName2;
        String cityName2 = this.cityName2;
        String address2 = this.address2;
        if (StringUtil.isNullOrEmpty(this.stateName2)) {
            stateName2 = "";
        }
        if (StringUtil.isNullOrEmpty(this.cityName2)) {
            cityName2 = "";
        }
        if (StringUtil.isNullOrEmpty(this.address2)) {
            address2 = "";
        }
        return this.entireAddress2 = stateName2 + cityName2 + address2;
    }

    /**
     * 组装商品信息
     */
    public void assemblyGoodsInfo(List<OrderTakeAdr> orderTakeAdrs) {
        StringBuilder sb = new StringBuilder();

        for (OrderTakeAdr orderTakeAdr : orderTakeAdrs) {
            if (this.orderNo.equals(orderTakeAdr.getOrderNo())) {
                sb.append(orderTakeAdr.getGoodsDesc())
                        .append("/").append(orderTakeAdr.getPlateAmount() == null ? 0 : orderTakeAdr.getPlateAmount()).append("板")
                        .append("/").append(orderTakeAdr.getPieceAmount()).append("件")
                        .append("/").append("重量").append(orderTakeAdr.getWeight()).append("KG")
                        .append(",");
            }
        }
        this.goodsInfo = sb.substring(0, sb.length() - 1);
    }

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
}
