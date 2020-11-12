package com.jayud.tms.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.jayud.common.enums.OrderStatusEnum;
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

    @ApiModelProperty(value = "提货信息")
    private List<DriverOrderTakeAdrVO> pickUpGoodsLis = new ArrayList<>();

    @ApiModelProperty(value = "送货信息")
    private List<DriverOrderTakeAdrVO> receivingGoodsList = new ArrayList<>();

    @ApiModelProperty(value = "是否接单")
    private boolean isTakeOrders;


    public void setStatus(String status) {

        this.status = OrderStatusEnum.getDesc(status);

    }

    public void groupAddr(List<DriverOrderTakeAdrVO> list) {
        //类型(1提货 2收货)
        list.stream().filter(tmp -> this.orderNo.equals(tmp.getOrderNo())).forEach(tmp -> {
            if (tmp.getOprType() == 1) {
                pickUpGoodsLis.add(tmp);
            }
            if (tmp.getOprType() == 2) {
                receivingGoodsList.add(tmp);
            }
        });
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
                this.isTakeOrders = true;
            }
        });
    }
}
