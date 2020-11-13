package com.jayud.oms.model.vo;

import com.jayud.common.enums.OrderStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @ApiModelProperty(value = "录用费用明细(查看详细信息才会有数据)")
    private List<DriverOrderPaymentCostVO> employmentFeeDetails = new ArrayList<>();

    @ApiModelProperty(value = "是否费用已提交(查看详细信息才会有数据)")
    private Boolean isFeeSubmitted;

    @ApiModelProperty(value = "费用总计(查看详细信息才会有数据)")
    List<Map<String, String>> totalCost;


    /**
     * 计算总费用
     */
    public void calculateTotalCost() {
        if (CollectionUtils.isEmpty(employmentFeeDetails)) {
            return;
        }
        //分组
        Map<String, BigDecimal> map = new HashMap<>();
        for (DriverOrderPaymentCostVO employmentFeeDetail : employmentFeeDetails) {
            if (map.get(employmentFeeDetail.getCurrency()) == null) {
                map.put(employmentFeeDetail.getCurrency(), employmentFeeDetail.getAmount());
            } else {
                map.put(employmentFeeDetail.getCurrency(),
                        map.get(employmentFeeDetail.getCurrency()).add(employmentFeeDetail.getAmount()));
            }
        }
        //重组数据
        totalCost = new ArrayList<>();
        map.forEach((k, v) -> {
            Map<String, String> tmp = new HashMap<>();
            tmp.put("desc",k + "总计");
            tmp.put("totalAmount",v + k);
            totalCost.add(tmp);
        });
    }

    public void setStatus(String status) {
        this.status = OrderStatusEnum.getDesc(status);
    }

}
