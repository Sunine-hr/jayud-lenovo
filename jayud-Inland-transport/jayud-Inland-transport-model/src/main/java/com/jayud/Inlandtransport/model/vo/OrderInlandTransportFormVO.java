package com.jayud.Inlandtransport.model.vo;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.jayud.common.ApiResult;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ProcessStatusEnum;
import com.jayud.common.enums.TradeTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 内陆订单
 * </p>
 *
 * @author LDR
 * @since 2021-03-01
 */
@Data
@Slf4j
public class OrderInlandTransportFormVO extends Model<OrderInlandTransportFormVO> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "内陆订单主键")
    private Long id;

    @ApiModelProperty(value = "主订单编号")
    private String mainOrderNo;

    @ApiModelProperty(value = "主订单id")
    private String mainOrderId;

    @ApiModelProperty(value = "内陆订单编号")
    private String orderNo;

    @ApiModelProperty(value = "流程状态(0:进行中,1:完成,2:草稿,3.关闭)")
    private Integer processStatus;

    @ApiModelProperty(value = "状态描述")
    private String statusDesc;

    @ApiModelProperty(value = "流程状态描述")
    private String processStatusDesc;

    @ApiModelProperty(value = "状态(NL_0待接单,NL_1内陆接单,NL_1_1内陆接单驳回,NL_2内陆派车,NL_2_1内陆派车驳回,NL_3派车审核, NL_3_1派车审核不通过,NL_3_2派车审核驳回,L_4确认派车,NL_4_1确认派车驳回,NL_5车辆提货,NL_5_1车辆提货驳回,NL_6货物签收)")
    private String status;

    @ApiModelProperty(value = "状态(NL_0待接单,NL_1内陆接单,NL_1_1内陆接单驳回,NL_2内陆派车,NL_2_1内陆派车驳回,NL_3派车审核, NL_3_1派车审核不通过,NL_3_2派车审核驳回,L_4确认派车,NL_4_1确认派车驳回,NL_5车辆提货,NL_5_1车辆提货驳回,NL_6货物签收)")
    private String inlandStatusDesc;

//    @ApiModelProperty(value = "车型(1吨车 2柜车)")
//    private Integer vehicleType;

    @ApiModelProperty(value = "车型(3T 5t 8T 10T 12T 20GP 40GP 45GP..)")
    private String vehicleSize;

    @ApiModelProperty(value = "结算单位CODE")
    private String unitCode;

    @ApiModelProperty(value = "操作主体")
    private String legalName;

    @ApiModelProperty(value = "操作主体id(法人主体ID)")
    private Long legalEntityId;

    @ApiModelProperty(value = "子订单操作主体名称(法人主体名称)")
    private String subLegalName;

    @ApiModelProperty(value = "是否需要录入费用")
    private Boolean needInputCost;

//    @ApiModelProperty(value = "接单人(登录用户名)")
//    private String orderTaker;

//    @ApiModelProperty(value = "接单日期")
//    private LocalDateTime receivingOrdersDate;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "货物信息")
    private String goodsInfo;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "客户代码")
    private String customerCode;

    @ApiModelProperty(value = "订单业务人员名称")
    private String bizUname;

    @ApiModelProperty(value = "对应业务类型")
    private String bizCode;

    @ApiModelProperty(value = "订单类别")
    private String classCode;

    @ApiModelProperty(value = "供应商id")
    private Long supplierId;

    @ApiModelProperty(value = "供应商代码")
    private String defaultSupplierCode;

    @ApiModelProperty(value = "费用录用默认结算单位")
    private String defaultUnitCode;

    @ApiModelProperty(value = "结算单位code")
    private String subUnitCode;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    /**
     * 组装商品信息
     */
    public void assemblyGoodsInfo(List<GoodsVO> goodsList) {
        StringBuilder sb = new StringBuilder();

        for (GoodsVO goods : goodsList) {
            if (this.id.equals(goods.getBusinessId())
                    && BusinessTypeEnum.KY.getCode().equals(goods.getBusinessType())) {
                sb.append(goods.getName())
                        .append("/").append(goods.getPlateAmount() == null ? 0 : goods.getPlateAmount()).append(goods.getPlateUnit())
                        .append("/").append(goods.getBulkCargoAmount()).append(goods.getBulkCargoUnit())
                        .append("/").append("重量").append(goods.getTotalWeight()).append("KG")
                        .append(",");
            }
        }
        this.goodsInfo = sb.substring(0, sb.length() - 1);
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

    /**
     * 组装供应商数据
     */
    public void assemblySupplierInfo(JSONArray supplierInfo) {
        if (supplierInfo == null) {
            return;
        }
        for (int i = 0; i < supplierInfo.size(); i++) {
            JSONObject json = supplierInfo.getJSONObject(i);
            if (this.supplierId != null && this.supplierId.equals(json.getLong("id"))) { //供应商配对
                this.defaultSupplierCode = json.getStr("supplierCode");
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
        this.inlandStatusDesc = OrderStatusEnum.getDesc(status);
    }



    public void setProcessStatus(Integer processStatus) {
        this.processStatus = processStatus;
        this.statusDesc = ProcessStatusEnum.getDesc(processStatus);
        this.processStatusDesc=ProcessStatusEnum.getDesc(processStatus);
    }

    public void setSubUnitCode(String subUnitCode) {
        this.subUnitCode = subUnitCode;
        this.defaultUnitCode = subUnitCode;
    }

}
