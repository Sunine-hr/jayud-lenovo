package com.jayud.finance.vo.template.order;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.entity.OrderDeliveryAddress;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ProcessStatusEnum;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 快进快出订单，返回对象
 * </p>
 *
 * @author LLJ
 * @since 2021-06-10
 */
@Data
public class StorageFastOrderTemplate {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "提货时间", required = true)
    private String operationTime;

    @ApiModelProperty(value = "快进快出订单号")
    private String orderNo;

    @ApiModelProperty(value = "快进快出订单号")
    private String subOrderNo;

    @ApiModelProperty(value = "主订单号", required = true)
    private String mainOrderNo;

    @ApiModelProperty(value = "货物名称", required = true)
    private String goodsInfo;

    @ApiModelProperty(value = "件数", required = true)
    private Integer number;

    @ApiModelProperty(value = "客户")
    private String customerName;


    public void assemblyMainOrderData(Object mainOrderObjs) {
        if (mainOrderObjs == null) {
            return;
        }
        JSONArray mainOrders = new JSONArray(JSON.toJSONString(mainOrderObjs));
        for (int i = 0; i < mainOrders.size(); i++) {
            JSONObject json = mainOrders.getJSONObject(i);
            if (this.mainOrderNo.equals(json.getStr("orderNo"))) { //主订单配对
                this.customerName = json.getStr("customerName");
                LocalDateTime operationTime = json.get("operationTime", LocalDateTime.class);
                this.operationTime = DateUtils.LocalDateTime2Str(operationTime,DateUtils.DATE_PATTERN);
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

    public void assembleData(JSONObject jsonObject) {
        JSONArray fastGoodsFormList = jsonObject.getJSONArray("fastGoodsFormList");
        if (!CollectionUtils.isEmpty(fastGoodsFormList)) {
            StringBuilder sb = new StringBuilder();
            Integer number = 0;
            for (int i = 0; i < fastGoodsFormList.size(); i++) {
                JSONObject data = fastGoodsFormList.getJSONObject(i);
                String name = data.getStr("name");
                sb.append(name).append(",");
                number = number + data.getInt("number");
            }
            this.goodsInfo = sb.toString();
            this.number = number;
        }
    }
}
