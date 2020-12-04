package com.jayud.airfreight.model.vo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.airfreight.model.enums.AirOrderTermsEnum;
import com.jayud.airfreight.model.po.Goods;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.enums.OrderStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 空运订单列表
 * </p>
 *
 * @author LDR
 * @since 2020-11-30
 */
@Data
public class AirOrderFormVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "空运订单主键")
    private Long id;

    @ApiModelProperty(value = "主订单编号")
    private String mainOrderNo;

    @ApiModelProperty(value = "主订单id")
    private String mainOrderId;

    @ApiModelProperty(value = "空运订单编号")
    private String orderNo;

    @ApiModelProperty(value = "进出口类型(1：进口，2：出口)")
    private Integer impAndExpType;

    @ApiModelProperty(value = "业务类型(贸易方式0:CIF,1:DUU,2:FOB,3:DDP)")
    private String terms;

    @ApiModelProperty(value = "状态(k_0待接单,k_1空运接单,k_2订舱,k_3订单入仓, k_4确认提单,k_5确认离港,k_6确认到港,k_7海外代理k_8确认签收)")
    private String status;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "货物信息")
    private String goodsInfo;

    @ApiModelProperty(value = "出发机场")
    private String portDeparture;

    @ApiModelProperty(value = "目的地机场")
    private String portDestination;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "流程状态(0:进行中,1:完成)")
    private Integer processStatus;

    /**
     * 组装商品信息
     */
    public void assemblyGoodsInfo(List<Goods> goodsList) {
        StringBuilder sb = new StringBuilder();
        for (Goods goods : goodsList) {
            if (this.id.equals(goods.getBusinessId())
                    && BusinessTypeEnum.KY.getCode().equals(goods.getBusinessType())) {
                sb.append(goods.getName()).append("+")
                        .append(goods.getBulkCargoAmount()).append(goods.getBulkCargoUnit())
                        .append("+").append("重量").append(goods.getTotalWeight()).append("KG")
                        .append(",");
            }
        }
        this.goodsInfo = sb.substring(0, sb.length() - 1);
    }

    /**
     * @param customerObjs 远程客户对象集合
     */
    public void assemblyMainOrderData(Object customerObjs) {
        if (customerObjs == null) {
            return;
        }
        JSONArray mainOrders = JSONArray.parseArray(JSON.toJSONString(customerObjs));
        for (int i = 0; i < mainOrders.size(); i++) {
            JSONObject json = mainOrders.getJSONObject(i);
            if (this.mainOrderNo.equals(json.getString("orderNo"))) { //主订单配对
                this.customerName = json.getString("customerName");
                this.mainOrderId = json.getString("id");
                break;
            }
        }

    }


    public void setStatus(String status) {
        this.status = OrderStatusEnum.getDesc(status);
    }

    public void setTerms(Integer terms) {
        this.terms = AirOrderTermsEnum.getDesc(terms);
    }
}
