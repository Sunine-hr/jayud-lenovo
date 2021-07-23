package com.jayud.finance.vo.template.pay;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.jayud.common.entity.OrderDeliveryAddress;
import com.jayud.common.utils.DateUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <p>
 * 拖车订单表
 * </p>
 *
 * @author LLJ
 * @since 2021-03-01
 */
@Data
@Slf4j
public class TrailerOrderPayTemplate {


    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "序号", required = true)
    private String num;

    @ApiModelProperty(value = "提货日期", required = true)
    private String takeTimeStr;

    @ApiModelProperty(value = "订单号", required = true)
    private String mainOrderNo;

    @ApiModelProperty(value = "子订单编号", required = true)
    private String subOrderNo;

    @ApiModelProperty(value = "客户", required = true)
    private String customerName;

    @ApiModelProperty(value = "订舱号", required = true)
    private String so;

    @ApiModelProperty(value = "柜号", required = true)
    private String cabinetNumber;

    @ApiModelProperty(value = "封号", required = true)
    private String paperStripSeal;

    @ApiModelProperty(value = "车牌号", required = true)
    private String plateNumber;

    @ApiModelProperty(value = "车型尺寸", required = true)
    private String cabinetSize;

    @ApiModelProperty(value = "提柜地", required = true)
    private String portDeparture;

    @ApiModelProperty(value = "卸货地", required = true)
    private String pickUpAddress;

    @ApiModelProperty(value = "还柜地", required = true)
    private String portDestination;

//    @ApiModelProperty(value = "报关单号", required = true)
//    private String yunCustomsNo;


//    @ApiModelProperty(value = "件数", required = true)
//    private Integer totalPieceAmount;
//
//    @ApiModelProperty(value = "毛重(KGS)", required = true)
//    private Double totalWeight;


//    @ApiModelProperty(value = "操作主体", required = true)
//    private String legalName;

//    @ApiModelProperty(value = "结算单位", required = true)
//    private String unitCodeName;


    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "启运地/目的地")
    private String portName;

    @ApiModelProperty(value = "进出口类型(1：进口，2：出口)")
    private Integer impAndExpType;


//    @ApiModelProperty(value = "起运港/目的港代码")
//    private String portCode;


//    @ApiModelProperty(value = "提运单上传附件地址数组集合")
//    private List<FileView> billPics = new ArrayList<>();


//    @ApiModelProperty(value = "封条附件路径")
//    private String pssFilePath;
//
//    @ApiModelProperty(value = "封条附件名称")
//    private String pssFileName;

//    @ApiModelProperty(value = "柜号附件路径")
//    private String cnFilePath;
//
//    @ApiModelProperty(value = "柜号附件名称")
//    private String cnFileName;


//    @ApiModelProperty(value = "SO附件路径")
//    private String soFilePath;

//    @ApiModelProperty(value = "SO附件名称")
//    private String soFileName;

//    @ApiModelProperty(value = "到港时间")
//    private String arrivalTime;

//    @ApiModelProperty(value = "截仓期时间")
//    private String closingWarehouseTime;
//
//    @ApiModelProperty(value = "截柜租时间")
//    private String timeCounterRent;
//
//    @ApiModelProperty(value = "开仓时间")
//    private String openTime;
//
//    @ApiModelProperty(value = "截补料时间")
//    private String cuttingReplenishingTime;


    @ApiModelProperty(value = "订单地址信息")
    private List<OrderDeliveryAddress> deliveryAddresses;
//

    public void assembleData(JSONObject jsonObject) {
        JSONObject trailerDispatchInfo = jsonObject.getJSONObject("trailerDispatchInfoVO");
        if (trailerDispatchInfo != null) {
            this.plateNumber = trailerDispatchInfo.getStr("plateNumber");
        }
        this.assemblyAddresses();
    }

    public void assemblyMainOrderData(Object mainOrderObjs) {
        if (mainOrderObjs == null) {
            return;
        }
        JSONArray mainOrders = new JSONArray(JSON.toJSONString(mainOrderObjs));
        for (int i = 0; i < mainOrders.size(); i++) {
            JSONObject json = mainOrders.getJSONObject(i);
            if (this.mainOrderNo.equals(json.getStr("orderNo"))) { //主订单配对
                this.customerName = json.getStr("customerName");
//                this.contractNo = json.getStr("contractNo");
//                this.customerCode = json.getStr("customerCode");
//                this.mainOrderId = json.getLong("id");
//                this.bizUname = json.getStr("bizUname");
//                this.bizCode = json.getStr("bizCode");
//                this.classCode = json.getStr("classCode");
                break;
            }
        }
    }

    /**
     * 组装订单地址
     */
    public void assemblyAddresses() {
        if (this.deliveryAddresses == null) {
            return;
        }

        Integer totalPieceAmount = 0;
        Double totalWeight = 0.0;
//        Double volume = 0.0;
        StringBuilder addrs = new StringBuilder();
//        StringBuilder goodsInfo = new StringBuilder();

        for (OrderDeliveryAddress deliveryAddress : this.deliveryAddresses) {
            Double weight = deliveryAddress.getTotalWeight() == null ? 0.0 : deliveryAddress.getTotalWeight();
            Integer bulkCargoAmount = deliveryAddress.getBulkCargoAmount() == null ? 0 : deliveryAddress.getBulkCargoAmount();
            totalWeight += weight;
            totalPieceAmount += bulkCargoAmount;
            addrs.append(deliveryAddress.getAddress()).append(",");
        }

        //进口：起运地是港口---目的地是送货地址
        //出口：起运地是客户地址---目的地是港口
//        if (this.impAndExpType.equals(1)) { //进口
//            this.portDeparture = this.portName;
//            this.portDestination = addrs.toString();
//        } else {
//            this.portDeparture = addrs.toString();
//            this.portDestination = this.portName;
//        }
        this.portDeparture = this.portName;
        this.portDestination = this.portName;

//        this.goodsInfo = goodsInfo.toString();
        this.pickUpAddress = addrs.length() > 6 ? addrs.substring(0, 6) : addrs.toString();
        this.takeTimeStr = DateUtils.format(deliveryAddresses.get(0).getDeliveryDate(), "yyyy-MM-dd");
//        this.totalPieceAmount = totalPieceAmount;
//        this.totalWeight = totalWeight;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
        this.subOrderNo = orderNo;
    }

}
