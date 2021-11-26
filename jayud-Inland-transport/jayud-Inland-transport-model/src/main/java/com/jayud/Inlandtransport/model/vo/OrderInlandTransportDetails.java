package com.jayud.Inlandtransport.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.entity.OrderDeliveryAddress;
import com.jayud.common.enums.OrderAddressEnum;
import com.jayud.common.utils.FileView;
import com.jayud.common.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
public class OrderInlandTransportDetails extends Model<OrderInlandTransportDetails> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "内陆订单主键")
    private Long id;

    @ApiModelProperty(value = "第三方订单号")
    private String thirdPartyOrderNo;

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

    @ApiModelProperty(value = "车型(3T 5t 8T 10T 12T 20GP 40GP 45GP..)")
    private String vehicleSize;

    @ApiModelProperty(value = "结算单位CODE")
    private String unitCode;

    @ApiModelProperty(value = "结算单位名称")
    private String unitName;

    @ApiModelProperty(value = "接单法人")
    private String legalName;

    @ApiModelProperty(value = "法人主体ID")
    private Long legalEntityId;

    @ApiModelProperty(value = "是否需要录入费用")
    private Boolean needInputCost;

    @ApiModelProperty(value = "接单人(登录用户名)")
    private String orderTaker;

    @ApiModelProperty(value = "接单日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime receivingOrdersDate;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "派车信息")
    private OrderInlandSendCarsVO orderInlandSendCarsVO;

    @ApiModelProperty("提货地址")
    private List<OrderDeliveryAddress> pickUpAddressList = new ArrayList<>();

    @ApiModelProperty("送货地址")
    private List<OrderDeliveryAddress> orderDeliveryAddressList = new ArrayList<>();

    @ApiModelProperty("提货文件")
    private List<FileView> pickUpFile;

    @ApiModelProperty("送货文件")
    private List<FileView> deliveryFile;

    @ApiModelProperty(value = "操作部门id")
    private Long departmentId;
    @ApiModelProperty(value = "第三方订单号")
    private Long type;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public void assembleDeliveryAddress(List<OrderDeliveryAddress> deliveryAddresses) {
        if (deliveryAddresses == null) {
            return;
        }
        deliveryAddresses.forEach(e -> {
            if (this.id.equals(e.getBusinessId())) {
                if (OrderAddressEnum.PICK_UP.getCode().equals(e.getAddressType())) {
                    pickUpAddressList.add(e);
                    pickUpFile = new ArrayList<>();
                    pickUpFile.addAll(e.getFileViewList());
                }
                if (OrderAddressEnum.DELIVERY.getCode().equals(e.getAddressType())) {
                    orderDeliveryAddressList.add(e);
                    deliveryFile = new ArrayList<>();
                    deliveryFile.addAll(e.getFileViewList());
                }
            }
        });
    }
}
