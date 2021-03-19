package com.jayud.oms.model.vo.template.order;

import cn.hutool.core.collection.CollectionUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.utils.Utilities;
import com.jayud.oms.model.vo.InputOrderTakeAdrVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
//@Accessors(chain = true)
public class TmsOrderTemplate {

    @ApiModelProperty(value = "中港订单ID")
    private Long id;

    @ApiModelProperty(value = "中港订单号", required = true)
    private String orderNo;

    @ApiModelProperty(value = "提货时间", required = true)
    private String takeTimeStr;

    @ApiModelProperty(value = "车型", required = true)
    private String vehicleSize;

    @ApiModelProperty(value = "车牌号", required = true)
    private String licensePlate;

    @ApiModelProperty(value = "进出口类型", required = true)
    private String goodsTypeDesc;

    @ApiModelProperty(value = "通关口岸", required = true)
    private String portName;

    @ApiModelProperty(value = "订单状态", required = true)
    private String statusDesc;

    @ApiModelProperty(value = "操作主体", required = true)
    private String legalName;

    @ApiModelProperty(value = "结算单位", required = true)
    private String unitName;

    @ApiModelProperty(value = "货物信息", required = true)
    private String goodsInfo;

    @ApiModelProperty(value = "提货地址", required = true)
    private String pickUpAddress;

    @ApiModelProperty(value = "送货地址", required = true)
    private String deliveryAddr;

    @ApiModelProperty(value = "费用状态", required = true)
    private String cost;

    @ApiModelProperty(value = "头部")
    private List<Map<String, Object>> head;


    @ApiModelProperty("通关口岸CODE")
    private String portCode;

    @ApiModelProperty("结算单位CODE")
    private String unitCode;

    @ApiModelProperty("接单法人ID")
    private Long legalEntityId;

    @ApiModelProperty(value = "状态")
    private String status;


//    @ApiModelProperty(value = "六联单号")
//    private String encode;

//    @ApiModelProperty(value = "接单人")
//    private String jiedanUser;

//    @ApiModelProperty(value = "接单时间")
//    private String jiedanTimeStr;

//    @ApiModelProperty(value = "货物流向")
//    private Integer goodsType;

//    @ApiModelProperty(value = "香港车牌")
//    private String hkLicensePlate;

//    @ApiModelProperty(value = "车型(1吨车 2柜车)")
//    private Integer vehicleType;

//    @ApiModelProperty(value = "中转仓库ID")
//    private Long warehouseInfoId;

//    @ApiModelProperty(value = "1-装货 0-不需要装货")
//    private String isLoadGoods;

//    @ApiModelProperty(value = "1-卸货 0-不需要卸货")
//    private String isUnloadGoods;

//    @ApiModelProperty(value = "柜号")
//    private String cntrNo;

//    @ApiModelProperty(value = "车型/柜号")
//    private String modelAndCntrNo;

//    @ApiModelProperty(value = "运输公司")
//    private String supplierChName;


//    @ApiModelProperty(value = "司机姓名")
//    private String driverName;

//    @ApiModelProperty(value = "司机大陆电话")
//    private String driverPhone;

//    @ApiModelProperty(value = "司机香港电话")
//    private String driverHkPhone;

//    @ApiModelProperty(value = "过磅数")
//    private String carWeighNum;

//    @ApiModelProperty(value = "总件数")
//    private Integer totalAmount;

//    @ApiModelProperty(value = "总重量")
//    private Integer totalWeight;

//    @ApiModelProperty(value = "无缝单号")
//    private String seamlessNo;

//    @ApiModelProperty(value = "清关单号")
//    private String clearCustomsNo;

//    @ApiModelProperty(value = "装车要求")
//    private String remarks;

//    @ApiModelProperty(value = "香港清关接单法人")
//    private String hkLegalName;

//    @ApiModelProperty(value = "香港清关接单法人ID")
//    private Long hkLegalId;

//    @ApiModelProperty(value = "香港清关结算单位")
//    private String hkUnitCode;

//    @ApiModelProperty(value = "是否香港清关")
//    private String isHkClear;

//    @ApiModelProperty(value = "是否车辆过磅")
//    private Boolean isVehicleWeigh;

    //中转信息
//    @ApiModelProperty(value = "中转公司")
//    private String companyName;

//    @ApiModelProperty(value = "联系人")
//    private String contacts;

//    @ApiModelProperty(value = "电话")
//    private String contactNumber;

//    @ApiModelProperty(value = "地址")
//    private String address;

    @ApiModelProperty(value = "提货地址")
    @JsonIgnore
    private List<InputOrderTakeAdrVO> orderTakeAdrForms1 = new ArrayList<>();

    @ApiModelProperty(value = "卸货地址")
    @JsonIgnore
    private List<InputOrderTakeAdrVO> orderTakeAdrForms2 = new ArrayList<>();


    public void setOrderTakeAdrForms1(List<InputOrderTakeAdrVO> orderTakeAdrForms1) {
        this.assemblyPickUpInfo(orderTakeAdrForms1);
    }

    public void setOrderTakeAdrForms2(List<InputOrderTakeAdrVO> orderTakeAdrForms2) {
        this.assemblyDeliveryAddrInfo(orderTakeAdrForms2);
    }

    public void assemblyDeliveryAddrInfo(List<InputOrderTakeAdrVO> orderTakeAdrForms) {
        if (CollectionUtil.isEmpty(orderTakeAdrForms)) {
            return;
        }
        StringBuilder addrs = new StringBuilder();
        for (InputOrderTakeAdrVO inputOrderTakeAdrVO : orderTakeAdrForms) {
            addrs.append(inputOrderTakeAdrVO.getAddress())
                    .append(",");

        }
        this.deliveryAddr = addrs.toString();
    }


    public void assemblyPickUpInfo(List<InputOrderTakeAdrVO> orderTakeAdrForms) {
        if (CollectionUtil.isEmpty(orderTakeAdrForms)) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder addrs = new StringBuilder();
        StringBuilder takeTime = new StringBuilder();
        for (InputOrderTakeAdrVO inputOrderTakeAdrVO : orderTakeAdrForms) {
            addrs.append(inputOrderTakeAdrVO.getAddress())
                    .append(",");

            sb.append(inputOrderTakeAdrVO.getGoodsDesc())
                    .append("/")
                    .append(inputOrderTakeAdrVO.getPlateAmount() == null ? 0 : inputOrderTakeAdrVO.getPlateAmount()).append("板")
                    .append("/")
                    .append(inputOrderTakeAdrVO.getPieceAmount()).append("件数")
                    .append("/")
                    .append(inputOrderTakeAdrVO.getWeight()).append("kg")
                    .append(",");

            takeTime.append(inputOrderTakeAdrVO.getTakeTimeStr())
                    .append(",");
        }
        this.goodsInfo = sb.toString();
        this.pickUpAddress = addrs.toString();
        this.takeTimeStr = takeTime.toString();

    }

    public TmsOrderTemplate() {
        this.head = Utilities.assembleEntityHead(this.getClass());
    }

    public void setStatus(String status) {
        this.status = status;
        this.statusDesc = OrderStatusEnum.getDesc(status);
    }
}
