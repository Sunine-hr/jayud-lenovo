package com.jayud.oceanship.vo;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.ApiResult;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ProcessStatusEnum;
import com.jayud.common.enums.TradeTypeEnum;
import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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
@Slf4j
public class SeaOrderFormVO extends Model<SeaOrderFormVO> {

    private static final long serialVersionUID=1L;

//    @ApiModelProperty(value = "海运订单主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    //@ApiModelProperty(value = "海运订单id")
    private Long orderId;

    @ApiModelProperty(value = "主订单编号")
    private String mainOrderNo;

//    @ApiModelProperty(value = "主订单id")
    private String mainOrderId;

    @ApiModelProperty(value = "海运订单编号")
    private String orderNo;

    @ApiModelProperty(value = "截补料单号")
    private String repOrderNo;

    //状态(S_0待接单,S_1海运接单,S_2订船,S_3订单入仓, S_4提交补料,S_5草稿提单,S_6放单确认,S_7确认离港,S_8确认到港,S_9海外代理S_10确认签收)
    //@ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "订单流程状态")
    private String statusDesc;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    //@ApiModelProperty(value = "客户代码")
    private String customerCode;

    @ApiModelProperty(value = "货物信息")
    private String goodsInfo;

    //流程状态(0:进行中,1:完成,2:草稿,3.关闭)
    //@ApiModelProperty(value = "流程状态")
    private Integer processStatus;

    @ApiModelProperty(value = "状态")
    private String processStatusDesc;

//    @ApiModelProperty(value = "结算单位code")
    //@ApiModelProperty(value = "结算单位")
    private String unitCode;

    @ApiModelProperty(value = "结算单位")
    private String unitCodeName;

    //进出口类型(1：进口，2：出口)
    //@ApiModelProperty(value = "进出口类型")
    private Integer impAndExpType;

    @ApiModelProperty(value = "进出口类型")
    private String impAndExpTypeDesc;

    //贸易方式(0:FOB,1:CIF,2:DAP,3:FAC,4:DDU,5:DDP)
    //@ApiModelProperty(value = "贸易方式")
    private Integer terms;

    @ApiModelProperty(value = "贸易方式")
    private String termsDesc;

    //@ApiModelProperty(value = "起运港代码")
    private String portDepartureCode;

    //@ApiModelProperty(value = "目的港代码")
    private String portDestinationCode;

    @ApiModelProperty(value = "起运港")
    private String portDepartureName;

    @ApiModelProperty(value = "目的港")
    private String portDestinationName;

    //@ApiModelProperty(value = "中转港代码")
    private String transitPortCode;

    @ApiModelProperty(value = "中转港")
    private String transitPort;

//    @ApiModelProperty(value = "船公司")
//    private String shipCompany;
//
//    @ApiModelProperty(value = "船名字")
//    private String shipName;
//
//    @ApiModelProperty(value = "船次")
//    private String shipNumber;
//
//    @ApiModelProperty(value = "预计离港时间")
//    private String etd;
//
//    @ApiModelProperty(value = "预计到港时间")
//    private String eta;
//
//    @ApiModelProperty(value = "入仓号")
//    private String warehousingNo;

    //@ApiModelProperty(value = "订船信息")
    private SeaBookshipVO seaBookShipForm = new SeaBookshipVO();

//    @ApiModelProperty(value = "海外供应商id")
    private Long overseasSuppliersId;

//    @ApiModelProperty(value = "代理服务类型（0:清关,1:配送）多个逗号隔开")
    //@ApiModelProperty(value = "代理服务类型")
    private String proxyServiceType;

//    @ApiModelProperty(value = "创建人(登录用户)")
    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTimeStr;

    //@ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    //是否需要录入费用(0:false,1:true)
    //@ApiModelProperty(value = "是否需要录入费用")
    private Boolean needInputCost;

    @ApiModelProperty(value = "业务员")
    private String bizUname;

    //@ApiModelProperty(value = "对应业务类型")
    private String bizCode;

    //@ApiModelProperty(value = "订单类别")
    private String classCode;

    //@ApiModelProperty(value = "法人主体id")
    private Long legalEntityId;

    @ApiModelProperty(value = "法人主体名称")
    private String subLegalName;

    //@ApiModelProperty(value = "是否录用费用")
    private Boolean cost;

    //@ApiModelProperty(value = "供应商id")
    //private Long supplierId;

    //@ApiModelProperty(value = "供应商代码")
    private String defaultSupplierCode;

    //@ApiModelProperty(value = "费用录用默认结算单位")
    private String defaultUnitCode;

    @ApiModelProperty(value = "货好时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime goodTime;

    //@ApiModelProperty(value = "运费是否到付(1代表true,0代表false)")
    private Boolean isFreightCollect;

    //@ApiModelProperty(value = "其他费用是否到付(1代表true,0代表false)")
    private Boolean isOtherExpensesPaid;

    //@ApiModelProperty(value = "是否危险品(1代表true,0代表false)")
    private Boolean isDangerousGoods;

    //@ApiModelProperty(value = "是否带电(1代表true,0代表false)")
    private Boolean isCharged;

    //@ApiModelProperty(value = "柜型大小")
    private Integer cabinetSize;

    //@ApiModelProperty(value = "柜型类型")
    private Integer cabinetType;

    @ApiModelProperty(value = "柜型大小")
    private String cabinetSizeName;

    @ApiModelProperty(value = "柜型类型")
    private String cabinetTypeName;

    @ApiModelProperty(value = "柜型数量")
    private List<CabinetSizeNumberVO> cabinetSizeNumbers;

    @ApiModelProperty(value = "截补料时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cutReplenishTime;

    //@ApiModelProperty(value = "发货地址")
    private List<OrderAddressVO> deliveryAddress;

    //@ApiModelProperty(value = "收货地址")
    private List<OrderAddressVO> shippingAddress;

    //@ApiModelProperty(value = "通知地址")
    private List<OrderAddressVO> notificationAddress;

    //@ApiModelProperty(value = "货品信息")
    private List<GoodsVO> goodsVOS;

    //@ApiModelProperty(value = "提单文件路径(多个逗号隔开)")
    private String filePath;

    //@ApiModelProperty(value = "提单文件名称(多个逗号隔开)")
    private String fileName;

    //@ApiModelProperty(value = "附件")
    private List<FileView> fileViewList = new ArrayList<>();

    //@ApiModelProperty(value = "结算部门")
    private Long unitDepartmentId;

    //@ApiModelProperty(value = "补料信息集合")
    private List<SeaReplenishmentVO> seaReplenishments;

    //@ApiModelProperty(value = "补料信息")
    private SeaReplenishmentVO seaReplenishment;

    @ApiModelProperty(value = "审核意见")
    private String auditOpinion;

    //@ApiModelProperty(value = "审核状况 1为审核通过;2为审核不通过")
    private Integer auditStatus;

    /**
     * 组装商品信息
     */
    public void assemblyGoodsInfo(List<GoodsVO> goodsList) {
        StringBuilder sb = new StringBuilder();

        for (GoodsVO goods : goodsList) {
            if (this.id.equals(goods.getBusinessId())
                    && BusinessTypeEnum.HY.getCode().equals(goods.getBusinessType())) {
                sb.append(goods.getName())
                        .append(" ").append(goods.getPlateAmount() == null ? 0 : goods.getPlateAmount()).append(goods.getPlateUnit())
                        .append(",").append(goods.getBulkCargoAmount()).append(goods.getBulkCargoUnit())
                        .append(",").append("重量:").append(goods.getTotalWeight()).append("KG")
                        .append(";");
            }
        }

        this.goodsInfo = sb.toString();
    }

    /**
     * @param mainOrderObjs 远程客户对象集合
     */
    public void assemblyMainOrderData(Object mainOrderObjs) {
        if (mainOrderObjs == null) {
            return;
        }
        JSONArray mainOrders = new JSONArray(JSON.toJSONString(mainOrderObjs));
        for (int i = 0; i < mainOrders.size(); i++) {
            JSONObject json = mainOrders.getJSONObject(i);
            if (this.mainOrderNo.equals(json.getStr("orderNo"))) { //主订单配对
                this.customerName = json.getStr("customerName");
                this.customerCode = json.getStr("customerCode");
                this.mainOrderId = json.getStr("id");
                this.bizUname = json.getStr("bizUname");
                this.bizCode = json.getStr("bizCode");
                this.classCode = json.getStr("classCode");
                break;
            }
        }

    }

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

    /**
     * 组装供应商数据
     */
    public void assemblySupplierInfo(JSONArray supplierInfo) {
        if (supplierInfo == null) {
            return;
        }
        for (int i = 0; i < supplierInfo.size(); i++) {
            JSONObject json = supplierInfo.getJSONObject(i);
            if (this.seaBookShipForm.getAgentSupplierId() != null && this.seaBookShipForm.getAgentSupplierId().equals(json.getLong("id"))) { //供应商配对
                this.defaultSupplierCode = json.getStr("supplierCode");
                break;
            }
        }
    }

    /**
     * 组装结算单位数据
     */
    public void assemblyUnitCodeInfo(ApiResult unitCodeInfo) {
        if (unitCodeInfo == null) {
            return;
        }
        if (unitCodeInfo.getCode() != HttpStatus.SC_OK) {
            log.warn("请求结算单位信息失败");
            return;
        }
        JSONArray legalEntitys = new JSONArray(unitCodeInfo.getData());
        for (int i = 0; i < legalEntitys.size(); i++) {
            JSONObject json = legalEntitys.getJSONObject(i);
            if (this.unitCode.equals(json.getStr("idCode"))) { //法人主体配对
                this.unitCodeName = json.getStr("name");
                break;
            }
        }
    }


    /**
     * 组装法人主体
     *
     * @param legalEntityResult
     */
    public void assemblyLegalEntity(ApiResult legalEntityResult) {
        if (legalEntityResult == null) {
            return;
        }
        if (legalEntityResult.getCode() != HttpStatus.SC_OK) {
            log.warn("请求法人主体信息失败");
            return;
        }
        JSONArray legalEntitys = new JSONArray(legalEntityResult.getData());
        for (int i = 0; i < legalEntitys.size(); i++) {
            JSONObject json = legalEntitys.getJSONObject(i);
            if (this.legalEntityId.equals(json.getLong("id"))) { //法人主体配对
                this.subLegalName = json.getStr("legalName");
                break;
            }
        }
    }


    public void setStatus(String status) {
        this.status = status;
        this.statusDesc = OrderStatusEnum.getDesc(status);
    }

    public void setImpAndExpType(Integer impAndExpType) {
        this.impAndExpType = impAndExpType;
        this.impAndExpTypeDesc = TradeTypeEnum.getDesc(impAndExpType);
    }

    public void setProcessStatus(Integer processStatus) {
        this.processStatus = processStatus;
        this.processStatusDesc = ProcessStatusEnum.getDesc(processStatus);
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
        this.defaultUnitCode = unitCode;
    }

    public void assemblyCabinetInfo(List<CabinetSizeNumberVO> cabinetSizeNumberVOS) {
        StringBuilder sb = new StringBuilder();
        for (CabinetSizeNumberVO cabinetSizeNumberVO : cabinetSizeNumberVOS) {
            sb.append(cabinetSizeNumberVO.getCabinetTypeSize()).append("/").append(cabinetSizeNumberVO.getNumber()).append(" ");
        }
        this.cabinetSizeName = sb.toString();
    }
}
