package com.jayud.oms.model.vo;

import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
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
public class InputAirOrderVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "空运订单主键")
    private Long id;

    @ApiModelProperty(value = "主订单编号")
    private String mainOrderNo;

    @ApiModelProperty(value = "空运订单编号")
    private String orderNo;

    @ApiModelProperty(value = "第三方订单编号")
    private String thirdPartyOrderNo;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "流程状态")
    private Integer processStatus;

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
    private InputAirBookingVO airBookingVO;

    @ApiModelProperty(value = "空运订单发货地址信息")
    private List<InputOrderAddressVO> deliveryAddress;

    @ApiModelProperty(value = "空运订单收货地址信息")
    private List<InputOrderAddressVO> shippingAddress;

    @ApiModelProperty(value = "空运订单通知地址信息")
    private List<InputOrderAddressVO> notificationAddress;

    @ApiModelProperty(value = "货品信息")
    private List<InputGoodsVO> goodsForms;

    @ApiModelProperty(value = "所有附件信息")
    private List<FileView> allPics = new ArrayList<>();

    @ApiModelProperty(value = "接单人(登录用户名)")
    private String orderTaker;

    @ApiModelProperty(value = "接单日期")
    private String receivingOrdersDate;

    @ApiModelProperty(value = "海外代理供应商id")
    private Long overseasSuppliersId;

    @ApiModelProperty(value = "海外代理供应商")
    private String overseasSuppliers;

    @ApiModelProperty(value = "发票号(多个逗号隔开)")
    private String invoiceNo;
}
