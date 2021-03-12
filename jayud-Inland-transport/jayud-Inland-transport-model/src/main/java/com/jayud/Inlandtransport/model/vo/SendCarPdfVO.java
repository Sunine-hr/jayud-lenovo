package com.jayud.Inlandtransport.model.vo;

import cn.hutool.core.collection.CollectionUtil;
import com.jayud.Inlandtransport.model.po.OrderInlandSendCars;
import com.jayud.common.enums.OrderAddressEnum;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class SendCarPdfVO {

    @ApiModelProperty(value = "法人主体中文名")
    private String legalName;

    @ApiModelProperty(value = "法人主体英文名")
    private String enLegalName;

    @ApiModelProperty(value = "电话")
    private String tel;

    @ApiModelProperty(value = "传真")
    private String fax;

    @ApiModelProperty(value = "子订单号")
    private String orderNo;

    @ApiModelProperty(value = "接单时间")
    private String jiedanTimeStr;

    @ApiModelProperty(value = "提货信息")
    private List<TakeGoodsInfoVO> takeInfo1 = new ArrayList();

    @ApiModelProperty(value = "送货地址")
    private String deliveryAddress;

    @ApiModelProperty(value = "联系人")
    private String deliveryContacts;

    @ApiModelProperty(value = "联系电话")
    private String deliveryPhone;

    @ApiModelProperty(value = "装车要求")
    private String remarks;

    @ApiModelProperty(value = "大陆车牌")
    private String licensePlate;

    @ApiModelProperty(value = "香港车牌")
    private String hkLicensePlate;

    @ApiModelProperty(value = "司机电话")
    private String driverPhone;

    @ApiModelProperty(value = "车型(1-3T 2-5t 3-8T 4-10T)")
    private String vehicleSize;

    @ApiModelProperty(value = "车型(1吨车 2柜车)")
    private Integer vehicleType;

    @ApiModelProperty(value = "车型(1吨车 2柜车)")
    private String vehicleTypeDesc;

    @ApiModelProperty(value = "柜号")
    private String cntrNo;

    @ApiModelProperty(value = "通关口岸")
    private String portName;

    @ApiModelProperty(value = "进出口")
    private String goodsType;

    @ApiModelProperty(value = "接单人")
    private String jiedanUser;

    @ApiModelProperty(value = "接单人联系电话")
    private String jiedanPhone;

    @ApiModelProperty(value = "香港清关地址")
    private String clearCustomsAddress;

    @ApiModelProperty(value = "货物信息")
    private List<GoodsInfoVO> goddsInfos = new ArrayList<>();

    @ApiModelProperty(value = "总件数")
    private Integer totalPieceAmount = 0;

    @ApiModelProperty(value = "总重量")
    private Double totalWeight = 0.0;

    @ApiModelProperty(value = "总体积")
    private Double totalVolume = 0.0;

    @ApiModelProperty(value = "总体积")
    private Integer totalPlateAmount = 0;

    public void setVehicleType(Integer vehicleType) {
        this.vehicleType = vehicleType;
        if (this.vehicleType == 1) {
            this.vehicleTypeDesc = "吨车";
        } else if (this.vehicleType == 2) {
            this.vehicleTypeDesc = "吨车";
        }
    }

    public String setGoodsType() {
        if (!StringUtil.isNullOrEmpty(this.goodsType)) {
            if ("1".equals(this.goodsType)) {
                return "进口";
            } else if ("2".equals(this.goodsType)) {
                return "出口";
            }
        }
        return "";
    }

    public void assembleAddress(List<OrderAddressVO> orderAddressList) {
        if (CollectionUtil.isEmpty(orderAddressList)) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (OrderAddressVO orderAddressVO : orderAddressList) {
            if (OrderAddressEnum.PICK_UP.getCode().equals(orderAddressVO.getType())) {
                TakeGoodsInfoVO takeGoodsInfoVO = new TakeGoodsInfoVO();
                takeGoodsInfoVO.setAddress(orderAddressVO.getAddress());
                takeGoodsInfoVO.setContacts(orderAddressVO.getContacts());
                takeGoodsInfoVO.setPhone(orderAddressVO.getPhone());
                takeGoodsInfoVO.setTakeTimeStr(orderAddressVO.getDeliveryDate());
                takeGoodsInfoVO.setId(orderAddressVO.getId());
                takeGoodsInfoVO.setVehicleType(this.vehicleType);
                takeGoodsInfoVO.setVehicleSize(this.vehicleSize);
                takeGoodsInfoVO.setVehicleTypeDesc(this.vehicleTypeDesc);
                takeInfo1.add(takeGoodsInfoVO);
            }
            if (OrderAddressEnum.DELIVERY.getCode().equals(orderAddressVO.getType())) {
                //送货处理,多个地址拼装成字符串返回(格式:送货地址 联系人:xxx 手机号码)
                sb.append(orderAddressVO.getAddress()).append(" ")
                        .append("联系人:").append(orderAddressVO.getContacts())
                        .append(" ").append(orderAddressVO.getPhone())
                        .append("<br/>");
            }
        }
        this.deliveryAddress = sb.toString();
    }

    public void assembleCar(List<OrderInlandSendCars> sendCars) {
        if (CollectionUtil.isEmpty(sendCars)) {
            return;
        }
        OrderInlandSendCars sendCar = sendCars.get(0);
        this.licensePlate = sendCar.getLicensePlate();
        this.driverPhone = sendCar.getDriverPhone();
        this.remarks = sendCar.getRemarks();

    }

    public void assembleGoods(List<GoodsVO> goodsList, List<OrderAddressVO> orderAddressList) {
        if (CollectionUtil.isEmpty(goodsList)) {
            return;
        }
        Map<Long, OrderAddressVO> takeInfo = orderAddressList.stream().collect(Collectors.toMap(OrderAddressVO::getBindGoodsId, e -> e));
        for (GoodsVO goodsVO : goodsList) {
            OrderAddressVO orderAddressVO = takeInfo.get(goodsVO.getId());
            if (orderAddressVO != null && OrderAddressEnum.PICK_UP.getCode().equals(orderAddressVO.getType())) {
                this.totalPieceAmount += goodsVO.getBulkCargoAmount();
                this.totalVolume += (goodsVO.getVolume() == null ? 0 : goodsVO.getVolume());
                this.totalWeight += (goodsVO.getTotalWeight() == null ? 0 : goodsVO.getTotalWeight());
                this.totalPlateAmount += (goodsVO.getPlateAmount() == null ? 0 : goodsVO.getPlateAmount());
                GoodsInfoVO goodsInfoVO = new GoodsInfoVO();
                goodsInfoVO.setGoodsDesc(goodsVO.getName());
                goodsInfoVO.setPieceAmount(goodsVO.getBulkCargoAmount());
                goodsInfoVO.setVolume(goodsVO.getVolume());
                goodsInfoVO.setWeight(goodsVO.getTotalWeight());
                goodsInfoVO.setPlateAmount(goodsVO.getPlateAmount());
                goddsInfos.add(goodsInfoVO);
            }
        }

    }
}
