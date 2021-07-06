package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.jayud.mall.model.po.GoodsServiceCost;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(value = "客户商品审核")
@Data
public class CustomerGoodsAuditForm {

    @ApiModelProperty(value = "主键id，自增 list", position = 1)
    @JSONField(ordinal = 1)
    private List<Integer> ids;

    @ApiModelProperty(value = "审核状态代码：1-审核通过，0-等待审核，-1-审核不通过", position = 2)
    @JSONField(ordinal = 2)
    private Integer status;

    @ApiModelProperty(value = "报关编码(customs_data id_code)", position = 3)
    @JSONField(ordinal = 3)
    private String dataCode;

    @ApiModelProperty(value = "清关编码(customs_clearance id_code)", position = 4)
    @JSONField(ordinal = 4)
    private String clearanceCode;

    //商品服务费用
    @ApiModelProperty(value = "商品服务费用list")
    private List<GoodsServiceCost> goodsServiceCostList;

}