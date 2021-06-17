package com.jayud.storage.model.vo;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
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
@EqualsAndHashCode(callSuper = false)
public class StorageOutPickingListVO {

    @ApiModelProperty(value = "主键id")
    private Long  id;

    @ApiModelProperty(value = "主键id")
    private Long  orderId;

    @ApiModelProperty(value = "主订单号")
    private String mainOrderNo;

    @ApiModelProperty(value = "入库订单号")
    private String orderNo;

    @ApiModelProperty(value = "车牌信息")
    private String plateInformation;

    @ApiModelProperty(value = "出仓号")
    @NotNull(message = "出仓号不为空")
    private String warehouseNumber;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "出库商品对象集合")
    private List<WarehouseGoodsLocationVO> outWarehouseGoodsForms;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "客户代码")
    private String customerCode;

    @ApiModelProperty(value = "预计出库时间")
    private String  expectedDeliveryTime;

    @ApiModelProperty(value = "件数")
    private Integer totalNumber;

    @ApiModelProperty(value = "重量")
    private Double totalWeight;

    @ApiModelProperty(value = "主订单id")
    private String mainOrderId;

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
                break;
            }
        }

    }

    /**
     * 拼接总重量，总件数，出库时间
     * @param goodsFormList
     */
    public void assemblyPickingListData(List<WarehouseGoodsVO> goodsFormList) {
        StringBuffer str = new StringBuffer();
        Integer totalNumber = 0;
        Double totalWeight = 0.0;
        for (WarehouseGoodsVO warehouseGoodsVO : goodsFormList) {
            if(warehouseGoodsVO.getExpectedDeliveryTime() != null){
                str.append(warehouseGoodsVO.getExpectedDeliveryTime().toString().replace("T"," ")).append("  ");
            }
            if(warehouseGoodsVO.getNumber() != null){
                totalNumber = totalNumber + warehouseGoodsVO.getNumber();
            }
            if(warehouseGoodsVO.getWeight() != null){
                totalWeight = totalWeight + warehouseGoodsVO.getWeight();
            }

        }

        this.totalNumber = totalNumber;
        this.totalWeight = totalWeight;
        this.expectedDeliveryTime = str.toString();
    }

}
