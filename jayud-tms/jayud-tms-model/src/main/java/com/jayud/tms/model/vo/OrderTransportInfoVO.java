package com.jayud.tms.model.vo;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.jayud.tms.model.po.OrderSendCars;
import com.jayud.tms.model.po.OrderTakeAdr;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 中港运输订单详情
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "OrderTransport对象", description = "中港运输订单")
public class OrderTransportInfoVO extends Model<OrderTransportInfoVO> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "主订单(order_no)")
    private String mainOrderNo;

    @ApiModelProperty(value = "订单编号(生成规则product_classify code+随时数)")
    private String orderNo;

    @ApiModelProperty(value = "第三方订单号")
    private String thirdPartyOrderNo;

    @ApiModelProperty(value = "口岸code(port_info code)")
    private String portCode;

    @ApiModelProperty(value = "货物流向(1进口 2出口)")
    private Integer goodsType;

    @ApiModelProperty(value = "车型(1吨车 2柜车)")
    private Integer vehicleType;

    @ApiModelProperty(value = "车型(3T)")
    private String vehicleSize;

    @ApiModelProperty(value = "柜号")
    private String cntrNo;

    @ApiModelProperty(value = "柜号图片")
    private String cntrPic;

    @ApiModelProperty(value = "柜号图片名称")
    private String cntrPicName;

    @ApiModelProperty(value = "仓库代码(warehouse_info)")
    private Long warehouseInfoId;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "1-装货 0-不需要装货")
    private String isLoadGoods;

    @ApiModelProperty(value = "1-装货 0-不需要装货")
    private String isUnloadGoods;

    @ApiModelProperty(value = "结算单位")
    private String unitCode;

    @ApiModelProperty(value = "接单法人")
    private String legalName;

    @ApiModelProperty(value = "接单法人ID")
    private Long legalEntityId;

    @ApiModelProperty(value = "香港清关结算单位,选择了香港清关必填")
    private String hkUnitCode;

    @ApiModelProperty(value = "香港清关接单法人ID,选择了香港清关必填")
    private Long hkLegalId;

    @ApiModelProperty(value = "香港清关接单法人,选择了香港清关必填")
    private String hkLegalName;

    @ApiModelProperty(value = "是否香港清关 1-是 0-否,选择了香港清关必填")
    private String isHkClear;

    @ApiModelProperty(value = "香港清关司机ID")
    private Long driverInfoId;

    @ApiModelProperty(value = "香港清关车辆ID")
    private Long vehicleId;

    @ApiModelProperty(value = "无缝单号")
    private String seamlessNo;

    @ApiModelProperty(value = "香港清关")
    private String clearCustomsNo;

    @ApiModelProperty(value = "接单人")
    private String jiedanUser;

    @ApiModelProperty(value = "接单时间")
    private LocalDateTime jiedanTime;

    @ApiModelProperty(value = "过磅数")
    private Double carWeighNum;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "创建用户")
    private String createdUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime updatedTime;

    @ApiModelProperty(value = "修改用户")
    private String updatedUser;

    @ApiModelProperty(value = "是否需要录入费用")
    private Boolean needInputCost;

    @ApiModelProperty(value = "通关时间")
    private LocalDateTime goCustomsTime;

    @ApiModelProperty(value = "预计通关时间")
    private LocalDateTime preGoCustomsTime;

    @ApiModelProperty(value = "是否车辆过磅")
    private Boolean isVehicleWeigh;

    @ApiModelProperty(value = "创建人的类型(0:本系统,1:vivo)")
    private Integer createUserType;

    @ApiModelProperty(value = "提货地址")
    private List<OrderTakeAdrInfoVO> pickUpAddress = new ArrayList<>();

    @ApiModelProperty(value = "送货地址")
    private List<OrderTakeAdrInfoVO> deliveryAddress = new ArrayList<>();

    @ApiModelProperty(value = "派车信息")
    private OrderSendCarsInfoVO orderSendCars;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    public void assemblyAddr(List<OrderTakeAdrInfoVO> takeAdrsList) {
        if (CollectionUtils.isEmpty(takeAdrsList)) {
            return;
        }
        for (OrderTakeAdrInfoVO orderTakeAdr : takeAdrsList) {
            if (this.orderNo.equals(orderTakeAdr.getOrderNo())) {
                switch (orderTakeAdr.getOprType()) {
                    case 1:
                        this.pickUpAddress.add(orderTakeAdr);
                        break;
                    case 2:
                        this.deliveryAddress.add(orderTakeAdr);
                        break;
                }
            }
        }
    }

    public void assemblyVehicleInfos(Object vehicleInfos) {
        if (vehicleInfos == null || this.orderSendCars == null
                || this.orderSendCars.getVehicleId() == null) {
            return;
        }
        JSONArray jsonArray = new JSONArray(vehicleInfos);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if (this.orderSendCars.getVehicleId().equals(jsonObject.getLong("id"))) {
                this.orderSendCars.setPlateNumber(jsonObject.getStr("plateNumber"))
                        .setHkNumber(jsonObject.getStr("hkNumber"));
            }

        }
    }
}
