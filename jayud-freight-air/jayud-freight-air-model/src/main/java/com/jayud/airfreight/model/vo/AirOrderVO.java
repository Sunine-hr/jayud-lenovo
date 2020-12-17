package com.jayud.airfreight.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.airfreight.model.enums.AirOrderTermsEnum;
import com.jayud.common.enums.TradeTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 空运订单表
 * </p>
 *
 * @author LDR
 * @since 2020-11-30
 */
@Data
public class AirOrderVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "空运订单主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "主订单编号")
    private String mainOrderNo;

    @ApiModelProperty(value = "空运订单编号")
    private String orderNo;

    @ApiModelProperty(value = "第三方订单编号")
    private String thirdPartyOrderNo;

    @ApiModelProperty(value = "状态(k_0待接单,k_1空运接单,k_2订舱,k_3订单入仓, k_4确认提单,k_5确认离港,k_6确认到港,k_7海外代理k_8确认签收)")
    private String status;

    @ApiModelProperty(value = "流程状态(0:进行中,1:完成)")
    private Integer processStatus;

    @ApiModelProperty(value = "结算单位code")
    private String settlementUnitCode;

    @ApiModelProperty(value = "接单法人id")
    private Long legalId;

    @ApiModelProperty(value = "接单法人")
    private Long legalName;

    @ApiModelProperty(value = "进出口类型(1：进口，2：出口)")
    private Integer impAndExpType;

    @ApiModelProperty(value = "进出口类型")
    private String impAndExpTypeDesc;

    @ApiModelProperty(value = "贸易方式(0:CIF,1:DUU,2:FOB,3:DDP)")
    private Integer terms;

    @ApiModelProperty(value = "贸易方式描述")
    private String termsDesc;

    @ApiModelProperty(value = "起运港代码")
    private String portDepartureCode;

    @ApiModelProperty(value = "起运港")
    private String portDeparture;

    @ApiModelProperty(value = "目的港代码")
    private String portDestinationCode;

    @ApiModelProperty(value = "目的港")
    private String portDestination;

    @ApiModelProperty(value = "货好时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private String goodTime;

    @ApiModelProperty(value = "运费是否到付")
    private Boolean isFreightCollect = false;

    @ApiModelProperty(value = "其他费用是否到付")
    private Boolean isOtherExpensesPaid = false;

    @ApiModelProperty(value = "是否危险品")
    private Boolean isDangerousGoods = false;

    @ApiModelProperty(value = "是否带电")
    private Boolean isCharged = false;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "订舱信息")
    private AirBookingVO airBookingVO;

    @ApiModelProperty(value = "空运订单发货地址信息")
    private OrderAddressVO deliveryAddressInfo;

    @ApiModelProperty(value = "空运订单收货地址信息")
    private OrderAddressVO shippingAddressInfo;

    @ApiModelProperty(value = "空运订单通知地址信息")
    private OrderAddressVO notificationAddressInfo;

    @ApiModelProperty(value = "货品信息")
    private List<GoodsVO> goodsVOs;

    @ApiModelProperty(value = "接单人(登录用户名)")
    private String orderTaker;

    @ApiModelProperty(value = "接单日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private String receivingOrdersDate;

    public void processingAddress(OrderAddressVO addressVO) {
        switch (addressVO.getType()) {
            case 0:
                this.deliveryAddressInfo = addressVO;
                break;
            case 1:
                this.shippingAddressInfo = addressVO;
                break;
            case 2:
                this.notificationAddressInfo = addressVO;
                break;
        }
    }

    public void setImpAndExpType(Integer impAndExpType) {
        this.impAndExpType = impAndExpType;
        this.impAndExpTypeDesc = TradeTypeEnum.getDesc(impAndExpType);
    }

    public void setTerms(Integer terms) {
        this.terms = terms;
        this.termsDesc = AirOrderTermsEnum.getDesc(terms);
    }
}
