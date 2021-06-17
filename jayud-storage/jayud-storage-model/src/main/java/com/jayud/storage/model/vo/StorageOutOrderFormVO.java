package com.jayud.storage.model.vo;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.ApiResult;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ProcessStatusEnum;
import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpStatus;

import javax.validation.constraints.NotNull;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 出库订单表
 * </p>
 *
 * @author LLJ
 * @since 2021-04-19
 */
@Data
@Slf4j
@EqualsAndHashCode(callSuper = false)
public class StorageOutOrderFormVO {


    @ApiModelProperty(value = "主键id")
    private Long  id;

    @ApiModelProperty(value = "主键id")
    private Long  orderId;

    @ApiModelProperty(value = "入库订单号")
    private String orderNo;

    @ApiModelProperty(value = "主订单号")
    private String mainOrderNo;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "状态")
    private String statusDesc;

    @ApiModelProperty(value = "流程状态(0:进行中,1:完成,2:草稿,3.关闭)")
    private Integer processStatus;

    @ApiModelProperty(value = "结算单位code")
    @NotNull(message = "结算单位不为空")
    private String unitCode;

    @ApiModelProperty(value = "流程状态名")
    private String processStatusDesc;

    @ApiModelProperty(value = "结算单位姓名")
    private String unitCodeName;

    @ApiModelProperty(value = "接单法人名称")
    private String legalName;

    @ApiModelProperty(value = "接单法人id")
    @NotNull(message = "操作主体不为空")
    private Long legalEntityId;

    @ApiModelProperty(value = "操作部门id")
    @NotNull(message = "操作部门不为空")
    private Long departmentId;

    @ApiModelProperty(value = "操作部门")
    private String departmentName;

    @ApiModelProperty(value = "车牌信息")
    private String plateInformation;

    @ApiModelProperty(value = "出仓号")
    @NotNull(message = "入仓号不为空")
    private String warehouseNumber;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建时间")
    private String createdTimeStr;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "出库商品对象集合")
    private List<WarehouseGoodsVO> goodsFormList;

    @ApiModelProperty(value = "附件集合")
    private List<FileView> allPics;

    @ApiModelProperty(value = "接单人")
    private String orderTaker;

    @ApiModelProperty(value = "接单日期")
    private String receivingOrdersDate;

    @ApiModelProperty(value = "业务员")
    private String bizUname;

    //@ApiModelProperty(value = "对应业务类型")
    private String bizCode;

    //@ApiModelProperty(value = "订单类别")
    private String classCode;

    //@ApiModelProperty(value = "主订单id")
    private String mainOrderId;

    @ApiModelProperty(value = "货物信息")
    private String goodsInfo;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "客户代码")
    private String customerCode;

    @ApiModelProperty(value = "接单法人名称")
    private String subLegalName;

    @ApiModelProperty(value = "是否有费用详情")
    private Boolean cost;

    @ApiModelProperty(value = "重量")
    private String weight;

    @ApiModelProperty(value = "体积")
    private String volume;

    @ApiModelProperty(value = "货物名称")
    private String name;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "子订单结算单位")
    private String subUnitCode;

    @ApiModelProperty(value = "规格型号")
    private String specificationModel;

    @ApiModelProperty(value = "子订单结算单位")
    private String defaultUnitCode;

    @ApiModelProperty(value = "预计出库时间")
    private String expectedDeliveryTime;

    /**
     * 组装商品信息
     */
    public void assemblyGoodsInfo(List<WarehouseGoodsVO> goodsList) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        StringBuilder sb3 = new StringBuilder();
        StringBuilder sb4 = new StringBuilder();
        Double volume = 0.0;
        Double weight = 0.0;
        for (WarehouseGoodsVO goods : goodsList) {

            if(goods.getName() != null){
                sb1.append(goods.getName()).append(" ");
            }
            if(goods.getSku() != null){
                sb2.append(goods.getSku()).append(" ");
            }
            if(goods.getSpecificationModel() != null){
                sb3.append(goods.getSpecificationModel()).append(" ");
            }
            if(goods.getExpectedDeliveryTime()!=null){
                sb4.append(goods.getExpectedDeliveryTime().toString().replace("T"," ")).append(";");
            }
            if(goods.getName() != null){
                sb.append(goods.getName())
                        .append(" ").append(goods.getBoardNumber() == null ? 0 : goods.getBoardNumber()).append("板")
                        .append(",").append(goods.getNumber()).append("件")
                        .append(",").append(goods.getPcs()== null ? 0 : goods.getPcs()).append("pcs")
                        .append(",").append("重量:").append(goods.getWeight() == null ? 0 : goods.getWeight()).append("KG")
                        .append(";");
            }


            if(goods.getVolume()!=null){
                volume = volume + goods.getVolume();
            }
            if(goods.getWeight()!=null){
                weight = weight + goods.getWeight();
            }
        }
        DecimalFormat df = new DecimalFormat("0.0000");
        this.volume = df.format(volume);
        this.weight = df.format(weight);
        this.goodsInfo = sb.toString();
        this.name = sb1.toString();
        this.sku = sb2.toString();
        this.specificationModel = sb3.toString();
        this.expectedDeliveryTime = sb4.toString();
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
        JSONArray unitCodes = new JSONArray(unitCodeInfo.getData());
        for (int i = 0; i < unitCodes.size(); i++) {
            JSONObject json = unitCodes.getJSONObject(i);
            if (this.unitCode.equals(json.getStr("idCode"))) { //结算单位配对
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
                this.legalName = json.getStr("legalName");
                break;
            }
        }
    }


    public void setStatus(String status) {
        this.status = status;
        this.statusDesc = OrderStatusEnum.getDesc(status);
    }

    public void setProcessStatus(Integer processStatus) {
        this.processStatus = processStatus;
        this.processStatusDesc = ProcessStatusEnum.getDesc(processStatus);
    }

    public void setUnitCodeName(String unitCodeName) {
        this.unitCodeName=unitCodeName;
    }
}
