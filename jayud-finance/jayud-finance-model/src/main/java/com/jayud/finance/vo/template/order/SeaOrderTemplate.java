package com.jayud.finance.vo.template.order;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.StringUtils;
import com.jayud.finance.vo.InputGoodsVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 海运订单表
 * </p>
 *
 * @author LLJ
 * @since 2021-01-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SeaOrderTemplate {

    @ApiModelProperty(value = "序号")
    private Integer num;

    @ApiModelProperty(value = "海运订单主键")
    private Long id;

    @ApiModelProperty(value = "货好时间", required = true)
    private String goodTime;

    @ApiModelProperty(value = "订单号", required = true)
    private String mainOrderNo;

    @ApiModelProperty(value = "子订单号", required = true)
    private String subOrderNo;

    @ApiModelProperty(value = "客户", required = true)
    private String customerName;

    @ApiModelProperty(value = "合同号", required = true)
    private String contractNo;

    @ApiModelProperty(value = "ATD", required = true)
    private String atd;

    @ApiModelProperty(value = "船公司", required = true)
    private String shipCompany;

    @ApiModelProperty(value = "柜型", required = true)
    private String cabinetTypeSize;

    @ApiModelProperty(value = "柜号", required = true)
    private String cabinetNumber;

    @ApiModelProperty(value = "柜数", required = true)
    private Integer cabinetNum;

    @ApiModelProperty(value = "S/O", required = true)
    private String subNo;

    @ApiModelProperty(value = "提单号", required = true)
    private String billLadingNo;

    @ApiModelProperty(value = "CI NO", required = true)
    private String CINO;

    @ApiModelProperty(value = "起运港", required = true)
    private String portDeparture;

    @ApiModelProperty(value = "目的港", required = true)
    private String portDestination;

    @ApiModelProperty(value = "商品名称", required = true)
    private String goodsInfo;

    @ApiModelProperty(value = "提单(套)", required = true)
    private Integer billLading;

    @ApiModelProperty(value = "件数/箱", required = true)
    private Integer totalNum;

    @ApiModelProperty(value = "货值", required = true)
    private BigDecimal goodsValue;

//    @ApiModelProperty(value = "结算单位code")
//    private String unitCode;

//    @ApiModelProperty(value = "接单法人名称")
//    private String legalName;

//    @ApiModelProperty(value = "接单法人id")
//    private Long legalEntityId;

    @ApiModelProperty(value = "进出口类型(1：进口，2：出口)")
    private Integer impAndExpType;

    @ApiModelProperty(value = "贸易方式(0:FOB,1:CIF,2:DAP,3:FAC,4:DDU,5:DDP)")
    private Integer terms;

    @ApiModelProperty(value = "贸易方式(0:FOB,1:CIF,2:DAP,3:FAC,4:DDU,5:DDP)")
    private String termsDesc;

    @ApiModelProperty(value = "起运港代码")
    private String portDepartureCode;

    @ApiModelProperty(value = "目的港代码")
    private String portDestinationCode;

    @ApiModelProperty(value = "中转港代码")
    private String transitPortCode;

    @ApiModelProperty(value = "中转港代码")
    private String transitPort;

    @ApiModelProperty(value = "柜型大小")
    private Integer cabinetSize;

    @ApiModelProperty(value = "柜型类型")
    private Integer cabinetType;

    @ApiModelProperty(value = "柜型大小")
    private String cabinetSizeName;

    @ApiModelProperty(value = "柜型类型")
    private String cabinetTypeName;

    @ApiModelProperty(value = "海外供应商id")
    private Long overseasSuppliersId;

    @ApiModelProperty(value = "代理服务类型（0:清关,1:配送）多个逗号隔开")
    private String proxyServiceType;


    @ApiModelProperty(value = "运费是否到付(1代表true,0代表false)")
    private Boolean isFreightCollect;

    @ApiModelProperty(value = "其他费用是否到付(1代表true,0代表false)")
    private Boolean isOtherExpensesPaid;

    @ApiModelProperty(value = "是否危险品(1代表true,0代表false)")
    private Boolean isDangerousGoods;

    @ApiModelProperty(value = "是否带电(1代表true,0代表false)")
    private Boolean isCharged;

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

    @ApiModelProperty(value = "是否需要录入费用(0:false,1:true)")
    private Boolean needInputCost;

    @ApiModelProperty(value = "接单人(登录用户名)")
    private String orderTaker;

    @ApiModelProperty(value = "接单日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime receivingOrdersDate;

    @ApiModelProperty(value = "截补料时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cutReplenishTime;

    @ApiModelProperty(value = "结算部门")
    private Long unitDepartmentId;

    @ApiModelProperty(value = "审核意见")
    private String auditOpinion;

    @ApiModelProperty(value = "审核状况 1为审核通过;2为审核不通过")
    private Integer auditStatus;

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
     * 组装订舱
     *
     * @param seaBookShipObj
     */
    public void assemblySeaBookShip(Object seaBookShipObj) {
        if (seaBookShipObj == null) {
            return;
        }
        JSONObject seaBookShip = new JSONObject(seaBookShipObj);
        this.atd = seaBookShip.getStr("atd");
        this.shipCompany = seaBookShip.getStr("shipCompany");
    }

    /**
     * 组装柜型
     *
     * @param cabinetSizeNumbers
     */
    public void assemblyCabinetSizeNumber(Object cabinetSizeNumbers) {
        if (cabinetSizeNumbers == null) {
            return;
        }
        StringBuilder cabinetSize = new StringBuilder();
        Integer number = 0;
        JSONArray jsonArray = new JSONArray(cabinetSizeNumbers);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            String cabinetTypeSize = json.getStr("cabinetTypeSize");
            if (!StringUtils.isEmpty(cabinetTypeSize)) {
                cabinetSize.append(cabinetTypeSize).append(",");
            }
            number += json.getInt("number", 0);
        }

        this.cabinetTypeSize = cabinetSize.toString();
        this.cabinetNum = number;
    }

    /**
     * 组装货柜信息
     *
     * @param seaContainerInfos
     */
    public void assemblyContainerInfo(Object seaContainerInfos) {
        if (seaContainerInfos == null) {
            return;
        }
        StringBuilder cabinetNumber = new StringBuilder();
        JSONArray jsonArray = new JSONArray(seaContainerInfos);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            cabinetNumber.append(json.getStr("cabinetNumber")).append(",");
        }

        this.cabinetNumber = cabinetNumber.toString();
    }


    /**
     * 组装货柜信息
     *
     * @param seaReplenishments
     */
    public void assemblySeaReplenishments(Object seaReplenishments) {
        if (seaReplenishments == null) {
            return;
        }
        StringBuilder subNo = new StringBuilder();
        JSONArray jsonArray = new JSONArray(seaReplenishments);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            subNo.append(json.getStr("subNo")).append(",");
        }

        this.subNo = subNo.toString();
    }

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
                    && BusinessTypeEnum.HY.getCode().equals(goods.getBusinessType())) {
                if (!StringUtils.isEmpty(goods.getName())) {
                    sb.append(goods.getName()).append(",");
                }

                totalNum += goods.getBulkCargoAmount() == null ? 0 : goods.getBulkCargoAmount();
            }
        }
        this.goodsInfo = sb.toString();
        this.totalNum = totalNum;
    }

    public void assembleData(JSONObject jsonObject) {
        this.assemblySeaBookShip(jsonObject.getObj("seaBookShip"));
        this.assemblyCabinetSizeNumber(jsonObject.getObj("cabinetSizeNumbers"));
        JSONArray goodsVOS = jsonObject.getJSONArray("goodsVOS");
        this.assemblyGoodsInfo(goodsVOS.toList(InputGoodsVO.class));
        this.assemblyContainerInfo(jsonObject.getObj("seaContainerInfos"));
        this.assemblySeaReplenishments(jsonObject.getObj("seaReplenishments"));
        this.assemblySeaBills(jsonObject.getObj("seaBills"));
    }


    private void assemblySeaBills(Object seaBills) {
        if (seaBills == null) {
            return;
        }
        StringBuilder billLadingNo = new StringBuilder();
        JSONArray jsonArray = new JSONArray(seaBills);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            billLadingNo.append(json.getStr("orderNo")).append(",");
        }

        this.billLadingNo = billLadingNo.toString();
    }

    public void setGoodTime(String goodTime) {
        this.goodTime = DateUtils.format(goodTime, DateUtils.DATE_PATTERN);
    }
}
