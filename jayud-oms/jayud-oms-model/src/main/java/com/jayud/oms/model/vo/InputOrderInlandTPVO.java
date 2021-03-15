package com.jayud.oms.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.entity.OrderDeliveryAddress;
import com.jayud.common.enums.OrderAddressEnum;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

//    @ApiModelProperty(value = "流程状态(0:进行中,1:完成,2:草稿,3.关闭)")
//    private Integer processStatus;

    @ApiModelProperty(value = "运输状态")
    private String status;

    @ApiModelProperty(value = "运输状态描述")
    private String statusDesc;

//    @ApiModelProperty(value = "车型(1吨车 2柜车)")
//    private Integer vehicleType;


    @ApiModelProperty(value = "结算单位CODE")
    private String unitCode;

    @ApiModelProperty(value = "法人主体ID")
    private Long legalEntityId;

    @ApiModelProperty(value = "接单人(登录用户名)")
    private String orderTaker;

    @ApiModelProperty(value = "接单日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
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

    @ApiModelProperty(value = "所有附件信息")
    private List<FileView> allPics = new ArrayList<>();

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    public void setStatus(String status) {
        this.status = status;
        this.statusDesc = OrderStatusEnum.getDesc(status);
    }
}
