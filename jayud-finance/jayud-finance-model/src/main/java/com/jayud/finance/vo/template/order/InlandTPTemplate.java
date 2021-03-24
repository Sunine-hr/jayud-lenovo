package com.jayud.finance.vo.template.order;

import com.jayud.common.entity.OrderDeliveryAddress;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.utils.StringUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class InlandTPTemplate {

    @ApiModelProperty(value = "内陆订单主键")
    private Long id;

    @ApiModelProperty(value = "订单编号", required = true)
    private String orderNo;

    @ApiModelProperty(value = "提货时间", required = true)
    private String deliveryDate;

    @ApiModelProperty(value = "车型尺寸", required = true)
    private String vehicleSize;

    @ApiModelProperty(value = "车牌号", required = true)
    private String licensePlate;

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
    private String costDesc;

    @ApiModelProperty(value = "费用状态")
    private Boolean cost;

//    @ApiModelProperty(value = "流程状态(0:进行中,1:完成,2:草稿,3.关闭)")
//    private Integer processStatus;

//    @ApiModelProperty(value = "车型(1吨车 2柜车)")
//    private Integer vehicleType;

    @ApiModelProperty(value = "订单状态")
    private String status;

    @ApiModelProperty(value = "结算单位CODE")
    private String unitCode;

    @ApiModelProperty(value = "法人主体ID")
    private Long legalEntityId;

    @ApiModelProperty(value = "接单人(登录用户名)")
    private String orderTaker;

//    @ApiModelProperty(value = "接单日期")
//    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime receivingOrdersDate;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

//    @ApiModelProperty(value = "创建时间")
//    private LocalDateTime createTime;


    @ApiModelProperty(value = "运输公司")
    private String supplierName;

    @ApiModelProperty(value = "司机姓名")
    private String driverName;

    @ApiModelProperty(value = "司机电话")
    private String driverPhone;

    @ApiModelProperty(value = "总件数")
    private String totalNum;

    @ApiModelProperty(value = "总重量")
    private String totalWeight;


    @ApiModelProperty("提货地址")
    private List<OrderDeliveryAddress> pickUpAddressList;

    @ApiModelProperty("送货地址")
    private List<OrderDeliveryAddress> orderDeliveryAddressList;

    //    @ApiModelProperty(value = "所有附件信息")
//    private List<FileView> allPics = new ArrayList<>();

    @ApiModelProperty(value = "主订单编号")
    private String mainOrderNo;


    public void setStatus(String status) {
        this.status = status;
        this.statusDesc = OrderStatusEnum.getDesc(status);
    }

    public void setPickUpAddressList(List<OrderDeliveryAddress> pickUpAddressList) {
        StringBuilder pickUpAddressSb = new StringBuilder();
        StringBuilder goodsInfo = new StringBuilder();
        StringBuilder takeTime = new StringBuilder();
        pickUpAddressList.forEach(e -> {
            pickUpAddressSb.append(e.getAddress()).append(",");
            goodsInfo.append(e.getGoodsName())
                    .append("/").append(e.getPlateAmount() == null ? 0 : e.getPlateAmount())
                    .append(StringUtils.isEmpty(e.getPlateUnit()) ? "板" : e.getPlateUnit())
                    .append("/").append(e.getBulkCargoAmount())
                    .append(StringUtils.isEmpty(e.getBulkCargoUnit()) ? "件" : e.getBulkCargoUnit())
                    .append("/").append("重量").append(e.getTotalWeight()).append("KG")
                    .append(",");
            takeTime.append(e.getDeliveryDate()).append(",");
        });
        this.pickUpAddress = pickUpAddressSb.toString();
        this.goodsInfo = goodsInfo.toString();
        this.deliveryDate = takeTime.toString();
    }

    public void setOrderDeliveryAddressList(List<OrderDeliveryAddress> orderDeliveryAddressList) {
        StringBuilder orderDeliveryAddressSb = new StringBuilder();
        orderDeliveryAddressList.forEach(e -> {
            orderDeliveryAddressSb.append(e.getAddress()).append(",");
        });
        this.deliveryAddr = orderDeliveryAddressSb.toString();
    }

    public void setCost(Boolean cost) {
        this.cost = cost;
        if (cost){
            this.costDesc="是";
        }else {
            this.costDesc="否";
        }
    }

}
