package com.jayud.oms.model.vo.template.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.utils.FileView;
import com.jayud.oms.model.bo.InputAirOrderForm;
import com.jayud.oms.model.vo.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AirOrderTemplate {

    @ApiModelProperty(value = "空运订单主键")
    private Long id;

    @ApiModelProperty(value = "订单编号", required = true)
    private String orderNo;

    @ApiModelProperty(value = "货好时间", required = true)
    private String goodTime;

    @ApiModelProperty(value = "进出口类型", required = true)
    private String impAndExpTypeDesc;

    @ApiModelProperty(value = "贸易方式", required = true)
    private String termsDesc;

    @ApiModelProperty(value = "状态", required = true)
    private String statusDesc;

    @ApiModelProperty(value = "操作主体", required = true)
    private String legalName;

    @ApiModelProperty(value = "结算单位", required = true)
    private String unitName;

    @ApiModelProperty(value = "货物信息", required = true)
    private String goodsInfo;

    @ApiModelProperty(value = "出发机场", required = true)
    private String portDeparture;

    @ApiModelProperty(value = "目的机场", required = true)
    private String portDestination;

    @ApiModelProperty(value = "航空公司", required = true)
    private String airlineCompany;

    @ApiModelProperty(value = "航班", required = true)
    private String flight;

    @ApiModelProperty(value = "ETD", required = true)
    private String etd;

    @ApiModelProperty(value = "ETA", required = true)
    private String eta;

    @ApiModelProperty(value = "入仓号", required = true)
    private String warehousingNo;

    @ApiModelProperty(value = "提单号", required = true)
    private String mainNo;

    @ApiModelProperty(value = "费用状态", required = true)
    private String costDesc;

    @ApiModelProperty(value = "费用状态")
    private Boolean cost;


    @ApiModelProperty(value = "结算单位code")
    private String settlementUnitCode;

    @ApiModelProperty(value = "接单法人id")
    private Long legalEntityId;

    @ApiModelProperty(value = "进出口类型(1：进口，2：出口)")
    private Integer impAndExpType;

    @ApiModelProperty(value = "贸易方式(0:CIF,1:DDU,2:FOB,3:DDP)")
    private Integer terms;

//    @ApiModelProperty(value = "创建人(登录用户)")
//    private String createUser;


//    @ApiModelProperty(value = "空运订单发货地址信息")
//    private List<InputOrderAddressVO> deliveryAddress;
//
//    @ApiModelProperty(value = "空运订单收货地址信息")
//    private List<InputOrderAddressVO> shippingAddress;
//
//    @ApiModelProperty(value = "空运订单通知地址信息")
//    private List<InputOrderAddressVO> notificationAddress;

//    @ApiModelProperty(value = "货品信息")
//    private List<InputGoodsVO> goodsForms;

//    @ApiModelProperty(value = "所有附件信息")
//    private List<FileView> allPics = new ArrayList<>();

//    @ApiModelProperty(value = "接单人(登录用户名)")
//    private String orderTaker;
//
//    @ApiModelProperty(value = "接单日期")
//    private String receivingOrdersDate;

//    @ApiModelProperty(value = "海外代理供应商id")
//    private Long overseasSuppliersId;
//
//    @ApiModelProperty(value = "海外代理供应商")
//    private String overseasSuppliers;

    @ApiModelProperty(value = "发票号(多个逗号隔开)")
    private String invoiceNo;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "货品信息")
    @JsonIgnore
    private List<InputGoodsVO> goodsForms;

    @ApiModelProperty(value = "主订单编号")
    private String mainOrderNo;

    public void setGoodsForms(List<InputGoodsVO> goodsForms) {
        StringBuilder sb = new StringBuilder();
        for (InputGoodsVO goodsForm : goodsForms) {
            sb.append(goodsForm.getName())
                    .append("/").append(goodsForm.getPlateAmount() == null ? 0 : goodsForm.getPlateAmount()).append(goodsForm.getPlateUnit())
                    .append("/").append(goodsForm.getBulkCargoAmount()).append(goodsForm.getBulkCargoUnit())
                    .append("/").append("重量").append(goodsForm.getTotalWeight()).append("KG")
                    .append(",");
        }
        this.goodsInfo = sb.toString();
    }


    public void assemblyData(InputAirOrderVO airOrderForm) {
        InputAirBookingVO airBookingVO = airOrderForm.getAirBookingVO();
        if (airBookingVO != null) {
            this.airlineCompany = airBookingVO.getAirlineCompany();
            this.flight = airBookingVO.getFlight();
            this.eta = airBookingVO.getEta();
            this.etd = airBookingVO.getEtd();
            this.warehousingNo = airBookingVO.getWarehousingNo();
            this.mainNo = airBookingVO.getMainNo();
        }

    }

    public void setStatus(String status) {
        this.status = status;
        this.statusDesc = OrderStatusEnum.getDesc(status);
    }

    public void setCost(Boolean cost) {
        this.cost = cost;
        if (cost){
            this.costDesc="是";
        }else {
            this.costDesc="否";
        }
    }
}
