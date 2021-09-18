package com.jayud.Inlandtransport.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.jayud.common.entity.OrderDeliveryAddress;
import com.jayud.common.enums.OrderAddressEnum;
import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 * 内陆订单
 * </p>
 *
 * @author LDR
 * @since 2021-03-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class InputOrderInlandTPVO extends Model<InputOrderInlandTPVO> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "内陆订单主键")
    private Long id;

    @ApiModelProperty(value = "主订单编号")
    private String mainOrderNo;

    @ApiModelProperty(value = "内陆订单编号")
    private String orderNo;

    @ApiModelProperty(value = "流程状态(0:进行中,1:完成,2:草稿,3.关闭)")
    private Integer processStatus;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "车型(1吨车 2柜车)")
    private Integer vehicleType;

    @ApiModelProperty(value = "结算单位CODE")
    private String unitCode;

    @ApiModelProperty(value = "法人主体ID")
    private Long legalEntityId;

    @ApiModelProperty(value = "接单人(登录用户名)")
    private String orderTaker;

    @ApiModelProperty(value = "接单日期")
    private LocalDateTime receivingOrdersDate;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

//    @ApiModelProperty(value = "创建时间")
//    private LocalDateTime createTime;

    @ApiModelProperty(value = "车型尺寸(3T 5t 8T 10T 12T 20GP 40GP 45GP..)")
    private String vehicleSize;

    @ApiModelProperty(value = "运输公司")
    private String supplierName;

    @ApiModelProperty(value = "车牌号")
    private String licensePlate;

    @ApiModelProperty(value = "司机姓名")
    private String driverName;

    @ApiModelProperty(value = "司机电话")
    private String driverPhone;

    @ApiModelProperty(value = "总件数")
    private String totalNum;

    @ApiModelProperty(value = "总重量")
    private String totalWeight;

    @ApiModelProperty(value = "接单法人")
    private String legalName;

    @ApiModelProperty(value = "结算单位名称")
    private String unitName;

    @ApiModelProperty("提货地址")
    private List<OrderDeliveryAddress> pickUpAddressList;

    @ApiModelProperty("送货地址")
    private List<OrderDeliveryAddress> orderDeliveryAddressList;

    @ApiModelProperty("提货文件")
    private List<FileView> pickUpFile;

    @ApiModelProperty("送货文件")
    private List<FileView> deliveryFile;

    @ApiModelProperty(value = "操作部门id")
    private Long departmentId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    public void setOrderInlandSendCarsVO(OrderInlandSendCarsVO orderInlandSendCarsVO) {
        this.supplierName = orderInlandSendCarsVO.getSupplierName();
        this.licensePlate = orderInlandSendCarsVO.getLicensePlate();
        this.driverName = orderInlandSendCarsVO.getDriverName();
        this.driverPhone = orderInlandSendCarsVO.getDriverPhone();
    }

    public void setPickUpAddressList(List<OrderDeliveryAddress> pickUpAddressList) {
        this.pickUpAddressList = pickUpAddressList;
        Double totalWeight = 0.0;
        Integer bulkCargoAmount = 0;
        Integer plateAmount = 0;
        for (OrderDeliveryAddress orderDeliveryAddress : pickUpAddressList) {
            totalWeight += orderDeliveryAddress.getTotalWeight() == null ? 0 : orderDeliveryAddress.getTotalWeight();
            bulkCargoAmount += orderDeliveryAddress.getBulkCargoAmount() == null ? 0 : orderDeliveryAddress.getBulkCargoAmount();
            ;
            plateAmount += (orderDeliveryAddress.getPlateAmount() == null ? 0 : orderDeliveryAddress.getPlateAmount());
        }
        this.totalNum = plateAmount + "板数" + bulkCargoAmount + "件";
        this.totalWeight = totalWeight + "KG";
    }

    public void assembleOrderInlandSendCarsVO(OrderInlandSendCarsVO sendCarsVO) {
        if (sendCarsVO == null) {
            return;
        }
        this.driverName = sendCarsVO.getDriverName();
        this.driverPhone = sendCarsVO.getDriverPhone();
        this.licensePlate = sendCarsVO.getLicensePlate();
        this.supplierName = sendCarsVO.getSupplierName();
    }


//    public void assembleDeliveryAddress(List<OrderDeliveryAddress> deliveryAddresses) {
//        AtomicReference<Double> totalWeight = new AtomicReference<>(0.0);
//        AtomicReference<Integer> bulkCargoAmount = new AtomicReference<>(0);
//        AtomicReference<Integer> plateAmount = new AtomicReference<>(0);
//        deliveryAddresses.forEach(e -> {
//            if (OrderAddressEnum.PICK_UP.getCode().equals(e.getAddressType())) {
//                totalWeight.updateAndGet(v -> v + e.getTotalWeight());
//                bulkCargoAmount.updateAndGet(v -> v + e.getBulkCargoAmount());
//                plateAmount.updateAndGet(v -> v + (e.getPlateAmount() == null ? 0 : e.getPlateAmount()));
//                pickUpAddressList.add(e);
//                pickUpFile.addAll(e.getFileViewList());
//            }
//            if (OrderAddressEnum.DELIVERY.getCode().equals(e.getAddressType())) {
//                orderDeliveryAddressList.add(e);
//                deliveryFile.addAll(e.getFileViewList());
//            }
//        });
//        this.totalNum = plateAmount.get() + "板数" + bulkCargoAmount + "件";
//        this.totalWeight = totalWeight.get() + "KG";
//    }
}
