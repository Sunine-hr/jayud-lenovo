package com.jayud.airfreight.model.vo;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.airfreight.model.enums.AirOrderTermsEnum;
import com.jayud.common.ApiResult;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ProcessStatusEnum;
import com.jayud.common.enums.TradeTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 空运订单+订舱数据
 * </p>
 *
 * @author LDR
 * @since 2020-11-30
 */
@Data
@Slf4j
public class AirOrderInfoVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "空运订单主键")
    private Long id;

    @ApiModelProperty(value = "主订单编号")
    private String mainOrderNo;

    @ApiModelProperty(value = "主订单id")
    private Long mainOrderId;

    @ApiModelProperty(value = "空运订单编号")
    private String orderNo;

    @ApiModelProperty(value = "进出口类型(1：进口，2：出口)")
    private Integer impAndExpType;

    @ApiModelProperty(value = "进出口类型描述")
    private String impAndExpTypeDesc;

    @ApiModelProperty(value = "业务类型(贸易方式0:CIF,1:DDU,2:FOB,3:DDP)")
    private String terms;

    @ApiModelProperty(value = "状态(A_0待接单,A_1空运接单,A_2订舱,A_3订单入仓,A_4确认提单,A_5确认离港,A_6确认到港,A_7海外代理,A_8确认签收)")
    private String status;

    @ApiModelProperty(value = "状态(A_0待接单,A_1空运接单,A_2订舱,A_3订单入仓,A_4确认提单,A_5确认离港,A_6确认到港,A_7海外代理,A_8确认签收)")
    private String subAirStatusDesc;

    @ApiModelProperty(value = "发票号")
    private String invoiceNo;

    @ApiModelProperty(value = "主单号")
    private String mainNo;

    @ApiModelProperty(value = "分单号")
    private String subNo;

    @ApiModelProperty(value = "计费重量")
    private Double billingWeight;

    @ApiModelProperty(value = "货好时间")
    private LocalDateTime goodTime;

//    @ApiModelProperty(value = "流程状态描述")
//    private String statusDesc;

//    @ApiModelProperty(value = "客户名称")
//    private String customerName;
//
//    @ApiModelProperty(value = "客户代码")
//    private String customerCode;

    @ApiModelProperty(value = "出发机场")
    private String portDeparture;

    @ApiModelProperty(value = "目的地机场")
    private String portDestination;

    @ApiModelProperty(value = "航空公司")
    private String airlineCompany;

    @ApiModelProperty(value = "航班号")
    private String flight;

    @ApiModelProperty(value = "预计离港时间")
    private String etd;

    @ApiModelProperty(value = "预计到港时间")
    private String eta;

    @ApiModelProperty(value = "实际离岗时间")
    private String atd;

    @ApiModelProperty(value = "入仓号")
    private String warehousingNo;


    @ApiModelProperty(value = "流程状态(0:进行中,1:完成,2:草稿,3.关闭)")
    private Integer processStatus;

    @ApiModelProperty(value = "流程状态描述")
    private String processStatusDesc;

    @ApiModelProperty(value = "订单业务人员名称")
    private String bizUname;

//    @ApiModelProperty(value = "对应业务类型")
//    private String bizCode;

//    @ApiModelProperty(value = "订单类别")
//    private String classCode;

    @ApiModelProperty(value = "结算单位code")
    private String subUnitCode;

    @ApiModelProperty(value = "法人主体id")
    private Long legalEntityId;

    @ApiModelProperty(value = "法人主体名称")
    private String subLegalName;

    @ApiModelProperty(value = "创建人的类型(0:本系统,1:vivo)")
    private Boolean createUserType;

    @ApiModelProperty(value = "供应商id")
    private Long supplierId;


    /**
     * 组装商品信息
     */
//    public void assemblyGoodsInfo(List<GoodsVO> goodsList) {
//        StringBuilder sb = new StringBuilder();
//
//        for (GoodsVO goods : goodsList) {
//            if (this.id.equals(goods.getBusinessId())
//                    && BusinessTypeEnum.KY.getCode().equals(goods.getBusinessType())) {
//                sb.append(goods.getName())
//                        .append("/").append(goods.getPlateAmount() == null ? 0 : goods.getPlateAmount()).append(goods.getPlateUnit())
//                        .append("/").append(goods.getBulkCargoAmount()).append(goods.getBulkCargoUnit())
//                        .append("/").append("重量").append(goods.getTotalWeight()).append("KG")
//                        .append(",");
//            }
//        }
//        this.goodsInfo = sb.toString();
//    }

    /**
     * @param mainOrderObjs 远程客户对象集合
     */
//    public void assemblyMainOrderData(Object mainOrderObjs) {
//        if (mainOrderObjs == null) {
//            return;
//        }
//        JSONArray mainOrders = new JSONArray(JSON.toJSONString(mainOrderObjs));
//        for (int i = 0; i < mainOrders.size(); i++) {
//            JSONObject json = mainOrders.getJSONObject(i);
//            if (this.mainOrderNo.equals(json.getStr("orderNo"))) { //主订单配对
//                this.customerName = json.getStr("customerName");
//                this.customerCode = json.getStr("customerCode");
//                this.mainOrderId = json.getLong("id");
//                this.bizUname = json.getStr("bizUname");
////                this.bizCode = json.getStr("bizCode");
////                this.classCode = json.getStr("classCode");
//                break;
//            }
//        }
//
//    }

    /**
     * 组装供应商数据
     */
//    public void assemblySupplierInfo(JSONArray supplierInfo) {
//        if (supplierInfo == null) {
//            return;
//        }
//        for (int i = 0; i < supplierInfo.size(); i++) {
//            JSONObject json = supplierInfo.getJSONObject(i);
//            if (this.supplierId != null && this.supplierId.equals(json.getLong("id"))) { //供应商配对
//                this.defaultSupplierCode = json.getStr("supplierCode");
//                break;
//            }
//        }
//    }

    /**
     * 组装法人主体
     *
     * @param legalEntityResult
     */
//    public void assemblyLegalEntity(ApiResult legalEntityResult) {
//        if (legalEntityResult == null) {
//            return;
//        }
//        if (legalEntityResult.getCode() != HttpStatus.SC_OK) {
//            log.warn("请求法人主体信息失败");
//            return;
//        }
//        JSONArray legalEntitys = new JSONArray(legalEntityResult.getData());
//        for (int i = 0; i < legalEntitys.size(); i++) {
//            JSONObject json = legalEntitys.getJSONObject(i);
//            if (this.legalEntityId.equals(json.getLong("id"))) { //法人主体配对
//                this.subLegalName = json.getStr("legalName");
//                break;
//            }
//        }
//    }
    public void setStatus(String status) {
        this.status = status;
        this.subAirStatusDesc = OrderStatusEnum.getDesc(status);
    }

    public void setTerms(Integer terms) {
        this.terms = AirOrderTermsEnum.getDesc(terms);
    }

    public void setImpAndExpType(Integer impAndExpType) {
        this.impAndExpType = impAndExpType;
        this.impAndExpTypeDesc = TradeTypeEnum.getDesc(impAndExpType);
    }

    public void setProcessStatus(Integer processStatus) {
        this.processStatus = processStatus;
//        this.statusDesc = ProcessStatusEnum.getDesc(processStatus);
//        this.processStatusDesc = ProcessStatusEnum.getDesc(processStatus);
    }

    public void setSubUnitCode(String subUnitCode) {
        this.subUnitCode = subUnitCode;
//        this.defaultUnitCode = subUnitCode;
    }
}
