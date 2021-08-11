package com.jayud.oms.model.vo;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.utils.StringUtils;
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

    @ApiModelProperty(value = "中港订单时间")
    private String time;

    @ApiModelProperty(value = "提货信息")
    private List<DriverOrderTakeAdrVO> pickUpGoodsList = new ArrayList<>();

    @ApiModelProperty(value = "送货信息")
    private DriverOrderTakeAdrVO receivingGoods;

    @ApiModelProperty(value = "是否接单")
    private Boolean acceptOrder;

    @ApiModelProperty(value = "录用费用明细(查看详细信息才会有数据)")
    private List<DriverOrderPaymentCostVO> employmentFeeDetails = new ArrayList<>();

    @ApiModelProperty(value = "是否费用已提交(查看详细信息才会有数据)")
    private Boolean isFeeSubmitted;

    @ApiModelProperty(value = "费用总计(查看详细信息才会有数据)")
    List<Map<String, String>> totalCost;

    @ApiModelProperty(value = "是否完成反馈状态")
    private Boolean isFeedbackFinish;

    @ApiModelProperty(value = "是否虚拟仓")
    private Boolean isVirtual;

    @ApiModelProperty(value = "应收费用状态")
    private String receivableCostStatus;

    @ApiModelProperty(value = "应付费用状态")
    private String paymentCostStatus;

    @ApiModelProperty(value = "接单记录状态")
    private String recordStatus;


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
            tmp.put("desc", k + "总计");
            tmp.put("totalAmount", v + k);
            totalCost.add(tmp);
        });
    }

    public void assemblyCostStatus(Map<String, Object> costStatus) {
        if (CollectionUtil.isEmpty(costStatus)) return;

        Map<String, Object> receivableCostStatus = (Map<String, Object>) costStatus.get("receivableCostStatus");
        Map<String, Object> paymentCostStatus = (Map<String, Object>) costStatus.get("paymentCostStatus");

        String receivableStatusDesc = MapUtil.getStr(receivableCostStatus, orderNo);
        String paymentStatusDesc = MapUtil.getStr(paymentCostStatus, orderNo);

        if (!StringUtils.isEmpty(receivableStatusDesc)) {
            String[] split = receivableStatusDesc.split("-", 2);
            this.receivableCostStatus = split[0];
        } else {
            this.receivableCostStatus = "未录入";
        }

        if (!StringUtils.isEmpty(paymentStatusDesc)) {
            String[] split = paymentStatusDesc.split("-", 2);
            this.paymentCostStatus = split[0];
        } else {
            this.paymentCostStatus = "未录入";
        }
    }


    public void setStatus(String status) {
        this.status = OrderStatusEnum.getDesc(status);
    }

}
