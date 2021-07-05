package com.jayud.airfreight.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.airfreight.model.enums.AirOrderTermsEnum;
import com.jayud.common.enums.ProcessStatusEnum;
import com.jayud.common.enums.TradeTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Arrays;
import java.util.Collections;
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

    @ApiModelProperty(value = "状态(A_0待接单,A_1空运接单,A_2订舱,A_3订单入仓,A_4确认提单,A_5确认离港,A_6确认到港,A_7海外代理,A_8确认签收)")
    private String status;

    @ApiModelProperty(value = "流程状态(0:进行中,1:完成,2:草稿,3.关闭)")
    private Integer processStatus;

    @ApiModelProperty(value = "流程状态描述")
    private String processStatusDesc;

    @ApiModelProperty(value = "结算单位code")
    private String settlementUnitCode;

    @ApiModelProperty(value = "接单法人id")
    private Long legalEntityId;

    @ApiModelProperty(value = "接单法人")
    private String legalName;

    @ApiModelProperty(value = "进出口类型(1：进口，2：出口)")
    private Integer impAndExpType;

    @ApiModelProperty(value = "进出口类型")
    private String impAndExpTypeDesc;

    @ApiModelProperty(value = "贸易方式(0:CIF,1:DDU,2:FOB,3:DDP)")
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
    private List<OrderAddressVO> deliveryAddress;

    @ApiModelProperty(value = "空运订单收货地址信息")
    private List<OrderAddressVO> shippingAddress;

    @ApiModelProperty(value = "空运订单通知地址信息")
    private List<OrderAddressVO> notificationAddress;

    @ApiModelProperty(value = "货品信息")
    private List<GoodsVO> goodsForms;

    @ApiModelProperty(value = "接单人(登录用户名)")
    private String orderTaker;

    @ApiModelProperty(value = "接单日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private String receivingOrdersDate;

    @ApiModelProperty(value = "发票号(多个逗号隔开)")
    private String invoiceNo;

    @ApiModelProperty(value = "操作部门id")
    private Long departmentId;

    public void processingAddress(OrderAddressVO addressVO) {
        switch (addressVO.getType()) {
            case 0:
                this.deliveryAddress = Collections.singletonList(addressVO);
                break;
            case 1:
                this.shippingAddress = Collections.singletonList(addressVO);
                break;
            case 2:
                this.notificationAddress = Collections.singletonList(addressVO);
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

    public void setProcessStatus(Integer processStatus) {
        this.processStatus = processStatus;
        this.processStatusDesc = ProcessStatusEnum.getDesc(processStatus);
    }
}
