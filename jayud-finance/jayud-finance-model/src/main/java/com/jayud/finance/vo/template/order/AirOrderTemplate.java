package com.jayud.finance.vo.template.order;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jayud.common.entity.OrderDeliveryAddress;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.finance.vo.InlandTP.OrderInlandSendCarsVO;
import com.jayud.finance.vo.InlandTP.OrderInlandTransportDetails;
import com.jayud.finance.vo.InputGoodsVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * <p>
 * 空运明细模板
 * </p>
 *
 * @author LDR
 * @since 2020-11-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AirOrderTemplate{

    @ApiModelProperty(value = "空运订单主键")
    private Long id;

    @ApiModelProperty(value = "货好时间", required = true)
    private String goodTime;

    @ApiModelProperty(value = "主订单编号", required = true)
    private String mainOrderNo;

    @ApiModelProperty(value = "空运订单编号", required = true)
    private String subOrderNo;

    @ApiModelProperty(value = "客户", required = true)
    private String customerName;

    @ApiModelProperty(value = "合同号", required = true)
    private String contractNo;

    //实际离岗时间
    @ApiModelProperty(value = "ATD", required = true)
    private String atd;

    @ApiModelProperty(value = "提单号", required = true)
    private String mainNo;

    //分单号
    @ApiModelProperty(value = "SO NO", required = true)
    private String subNo;

    //发票号
    @ApiModelProperty(value = "CI NO", required = true)
    private String invoiceNo;

    @ApiModelProperty(value = "启运地", required = true)
    private String portDeparture;

    @ApiModelProperty(value = "目的地", required = true)
    private String portDestination;

    @ApiModelProperty(value = "货物名称", required = true)
    private String goodsInfo;

    @ApiModelProperty(value = "件数", required = true)
    private Integer totalNum;

    @ApiModelProperty(value = "毛重(KGS)", required = true)
    private Double totalWeight;

    @ApiModelProperty(value = "体积/CBM", required = true)
    private Double volume;

    @ApiModelProperty(value = "计费重/KG", required = true)
    private Double billingWeight;

//    @ApiModelProperty(value = "进出口类型", required = true)
//    private String impAndExpTypeDesc;
//
//    @ApiModelProperty(value = "贸易方式", required = true)
//    private String termsDesc;
//
//    @ApiModelProperty(value = "状态", required = true)
//    private String statusDesc;
//
//    @ApiModelProperty(value = "操作主体", required = true)
//    private String legalName;
//
//    @ApiModelProperty(value = "结算单位", required = true)
//    private String unitName;



//
//    @ApiModelProperty(value = "航空公司", required = true)
//    private String airlineCompany;
//
//    @ApiModelProperty(value = "航班", required = true)
//    private String flight;
//
//    @ApiModelProperty(value = "ETD", required = true)
//    private String etd;
//
//    @ApiModelProperty(value = "ETA", required = true)
//    private String eta;
//
//    @ApiModelProperty(value = "入仓号", required = true)
//    private String warehousingNo;
//
//
//    @ApiModelProperty(value = "费用状态", required = true)
//    private String costDesc;
//
//    @ApiModelProperty(value = "结算单位code")
//    private String settlementUnitCode;
//
//    @ApiModelProperty(value = "接单法人id")
//    private Long legalEntityId;
//
//    @ApiModelProperty(value = "进出口类型(1：进口，2：出口)")
//    private Integer impAndExpType;
//
//    @ApiModelProperty(value = "贸易方式(0:CIF,1:DDU,2:FOB,3:DDP)")
//    private Integer terms;

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


    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "子订单编号")
    private String orderNo;


//    @ApiModelProperty(value = "货品信息")
//    @JsonIgnore
//    private List<InputGoodsVO> goodsForms;




    /**
     * 组装商品信息
     */
    public void assemblyGoodsInfo(List<InputGoodsVO> goodsList) {
        StringBuilder sb = new StringBuilder();
        Integer totalNum = 0;
        Double totalWeight = 0.0;
        Double volume = 0.0;
        for (InputGoodsVO goods : goodsList) {
            if (this.id.equals(goods.getBusinessId())
                    && BusinessTypeEnum.KY.getCode().equals(goods.getBusinessType())) {
                sb.append(goods.getName());
                totalNum += goods.getBulkCargoAmount() == null ? 0 : goods.getBulkCargoAmount();
                totalWeight += goods.getTotalWeight() == null ? 0 : goods.getTotalWeight();
                volume += goods.getVolume() == null ? 0 : goods.getVolume();
            }
        }
        this.goodsInfo = sb.toString();
        this.totalNum = totalNum;
        this.totalWeight = totalWeight;
        this.volume = volume;
    }


//    public void assemblyData(InputAirOrderVO airOrderForm) {
//        InputAirBookingVO airBookingVO = airOrderForm.getAirBookingVO();
//        if (airBookingVO != null) {
//            this.airlineCompany = airBookingVO.getAirlineCompany();
//            this.flight = airBookingVO.getFlight();
//            this.eta = airBookingVO.getEta();
//            this.etd = airBookingVO.getEtd();
//            this.warehousingNo = airBookingVO.getWarehousingNo();
//            this.mainNo = airBookingVO.getMainNo();
//        }

//    }


    public void assemblyMainOrderData(Object mainOrderObjs) {
        if (mainOrderObjs == null) {
            return;
        }
        JSONArray mainOrders = new JSONArray(JSON.toJSONString(mainOrderObjs));
        for (int i = 0; i < mainOrders.size(); i++) {
            JSONObject json = mainOrders.getJSONObject(i);
            if (this.mainOrderNo.equals(json.getStr("orderNo"))) { //主订单配对
                this.customerName = json.getStr("customerName");
                this.contractNo = json.getStr("contractNo");
//                this.customerCode = json.getStr("customerCode");
//                this.mainOrderId = json.getLong("id");
//                this.bizUname = json.getStr("bizUname");
//                this.bizCode = json.getStr("bizCode");
//                this.classCode = json.getStr("classCode");
                break;
            }
        }

    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
        this.subOrderNo = orderNo;
    }

//    public void assemblyOrderAddress(List<InputOrderAddressVO> orderAddressVOS) {
//        if (CollectionUtils.isEmpty(orderAddressVOS)) {
//            return;
//        }
//        StringBuilder placeDepartureSb = new StringBuilder();
//        for (InputOrderAddressVO orderAddressVO : orderAddressVOS) {
//            if (this.id.equals(orderAddressVO.getBusinessId())
//                    && BusinessTypeEnum.KY.getCode().equals(orderAddressVO.getBusinessType())) {
//
//                if (OrderAddressEnum.DELIVER_GOODS.getCode().equals(orderAddressVO.getType())) {
//
//                }
//
//
//            }
//        }
//
//    }

}
