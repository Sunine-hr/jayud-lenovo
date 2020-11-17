package com.jayud.tms.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jayud.common.enums.OrderStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 司机中港运输订单
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Data
public class DriverOrderTransportVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "中港订单id")
    private Long id;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "口岸名称")
    private String portName;

    @ApiModelProperty(value = "订单状态")
    private String status;

    @ApiModelProperty(value = "省（提货）")
    private String pickUpProvince;

    @ApiModelProperty(value = "市（提货）")
    private String pickUpCity;

    @ApiModelProperty(value = "区(提货)")
    private String pickUpArea;

    @ApiModelProperty(value = "省（送货）")
    private String receivingProvince;

    @ApiModelProperty(value = "市（送货）")
    private String receivingCity;

    @ApiModelProperty(value = "区(送货)")
    private String receivingArea;

    @ApiModelProperty(value = "货物信息")
    private String goodsDesc;

    @ApiModelProperty(value = "送货详细地址")
    @JsonIgnore
    private String address;

    @ApiModelProperty(value = "联系人")
    @JsonIgnore
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    @JsonIgnore
    private String contactNumber;

    @ApiModelProperty(value = "中港订单时间")
    @JsonFormat(timezone = "GMT+8", pattern = "MM-dd")
    private LocalDateTime time;

    @ApiModelProperty(value = "提货信息")
    private List<DriverOrderTakeAdrVO> pickUpGoodsList = new ArrayList<>();

    @ApiModelProperty(value = "送货信息集合")
    @JsonIgnore
    private List<DriverOrderTakeAdrVO> receivingGoodsList = new ArrayList<>();

    @ApiModelProperty(value = "送货信息")
    private DriverOrderTakeAdrVO receivingGoods;

    @ApiModelProperty(value = "是否接单")
    private boolean acceptOrder;


    public void setStatus(String status) {
        this.status = OrderStatusEnum.getDesc(status);
    }

    public void groupAddr(List<DriverOrderTakeAdrVO> list) {
        //类型(1提货 2收货)
        list.stream().filter(tmp -> this.orderNo.equals(tmp.getOrderNo())).forEach(tmp -> {
            if (tmp.getOprType() == 1) {
                pickUpGoodsList.add(tmp);
            }
            if (tmp.getOprType() == 2) {
                receivingGoodsList.add(tmp);
            }
        });
    }

    public void assemblyAddr() {
        if (receivingGoodsList.size() == 1) {
            DriverOrderTakeAdrVO receivingGoods = receivingGoodsList.get(0);
            this.receivingProvince = receivingGoods.getProvince();
            this.receivingCity = receivingGoods.getCity();
            this.receivingArea = receivingGoods.getArea();
            this.receivingGoods = receivingGoods;
        }
        if (receivingGoodsList.size() > 1) {
            receivingGoods = new DriverOrderTakeAdrVO();
            receivingGoods.setProvince(this.receivingProvince);
            receivingGoods.setCity(this.receivingCity);
            receivingGoods.setArea(this.receivingArea);
            receivingGoods.setAddress(this.address);
            receivingGoods.setPhone(this.contactNumber);
            receivingGoods.setContacts(this.contacts);
        }
        if (!CollectionUtils.isEmpty(pickUpGoodsList)) {
            DriverOrderTakeAdrVO pickUpGoods = pickUpGoodsList.get(0);
            this.pickUpProvince = pickUpGoods.getProvince();
            this.pickUpCity = pickUpGoods.getCity();
            this.pickUpArea = pickUpGoods.getArea();
        }
    }

    /**
     * 拼接商品名称
     */
    public void assemblyGoodsName() {
        StringBuilder sb = new StringBuilder();
        this.pickUpGoodsList.stream().filter(tmp -> StringUtils.isNotBlank(tmp.getGoodsDesc()))
                .forEach(tmp -> sb.append(tmp.getGoodsDesc()).append("+"));
        this.goodsDesc = sb.substring(0, sb.length() - 1);
    }

    /**
     * 处理已经接单的数据
     *
     * @param orderIds
     */
    public void setTakeOrders(List<Long> orderIds) {
        if (CollectionUtils.isEmpty(orderIds)) {
            return;
        }
        orderIds.forEach(tmp -> {
            if (this.id.equals(tmp)) {
                this.acceptOrder = true;
            }
        });
    }


}
