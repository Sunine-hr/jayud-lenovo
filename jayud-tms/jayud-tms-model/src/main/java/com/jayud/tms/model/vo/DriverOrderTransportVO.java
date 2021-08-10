package com.jayud.tms.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ApiModelProperty(value = "是否虚拟仓")
    private Boolean isVirtual;

    @ApiModelProperty(value = "是否接单")
    private boolean acceptOrder;


    public void setStatus(String status) {
        this.status = status;
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
//        if (receivingGoodsList.size() == 1) {
//            DriverOrderTakeAdrVO receivingGoods = receivingGoodsList.get(0);
//            this.receivingProvince = receivingGoods.getProvince();
//            this.receivingCity = StringUtils.isEmpty(receivingGoods.getArea()) ? receivingGoods.getProvince() : receivingGoods.getCity();
//            this.receivingArea = StringUtils.isEmpty(receivingGoods.getArea()) ? receivingGoods.getCity() : receivingGoods.getArea();
//            this.receivingGoods = receivingGoods;
//        }
//        if (receivingGoodsList.size() > 1) {
//            receivingGoods = new DriverOrderTakeAdrVO();
//            receivingGoods.setProvince(this.receivingProvince);
//            String city = "";
//            String area = "";
//            if (StringUtils.isEmpty(this.receivingArea)) {
//                city = this.receivingProvince;
//                area = this.receivingCity;
//            } else {
//                city = this.receivingCity;
//                area = this.receivingArea;
//            }
//            receivingGoods.setCity(city);
//            receivingGoods.setArea(area);
//            receivingGoods.setAddress(this.address);
//            receivingGoods.setPhone(this.contactNumber);
//            receivingGoods.setContacts(this.contacts);
//            //外部送货地址
//            this.receivingCity = city;
//            this.receivingArea = area;
//        }

        //虚拟仓展示多个地址
        if (isVirtual != null && isVirtual) {
            DriverOrderTakeAdrVO receivingGoods = receivingGoodsList.get(0);
            this.receivingProvince = receivingGoods.getProvince();
            this.receivingCity = StringUtils.isEmpty(receivingGoods.getArea()) ? receivingGoods.getProvince() : receivingGoods.getCity();
            this.receivingArea = StringUtils.isEmpty(receivingGoods.getArea()) ? receivingGoods.getCity() : receivingGoods.getArea();
            this.receivingGoods = receivingGoods;
        } else {
            receivingGoods = new DriverOrderTakeAdrVO();
            receivingGoods.setProvince(this.receivingProvince);
            String city = "";
            String area = "";
            if (StringUtils.isEmpty(this.receivingArea)) {
                city = this.receivingProvince;
                area = this.receivingCity;
            } else {
                city = this.receivingCity;
                area = this.receivingArea;
            }
            receivingGoods.setCity(city);
            receivingGoods.setArea(area);
            receivingGoods.setAddress(this.address);
            receivingGoods.setPhone(this.contactNumber);
            receivingGoods.setContacts(this.contacts);
            //外部送货地址
            this.receivingCity = city;
            this.receivingArea = area;
        }


        if (!CollectionUtils.isEmpty(pickUpGoodsList)) {
            DriverOrderTakeAdrVO pickUpGoods = pickUpGoodsList.get(0);
            this.pickUpProvince = pickUpGoods.getProvince();
            this.pickUpCity = StringUtils.isEmpty(pickUpGoods.getArea()) ? pickUpGoods.getProvince() : pickUpGoods.getCity();
            this.pickUpArea = StringUtils.isEmpty(pickUpGoods.getArea()) ? pickUpGoods.getCity() : pickUpGoods.getArea();
        }
    }

    /**
     * 拼接商品名称
     */
    public void assemblyGoodsName() {
        StringBuilder sb = new StringBuilder();
        this.pickUpGoodsList.stream().filter(tmp -> StringUtils.isNotBlank(tmp.getGoodsDesc()))
                .forEach(tmp -> sb.append(tmp.getGoodsDesc()).append("+"));
        if (sb.length() != 0) {
            this.goodsDesc = sb.substring(0, sb.length() - 1);
        }
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
