package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.jayud.mall.model.po.OceanCounterCustomerRelation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(value = "柜子", description = "1提单对应1货柜(PS:之前是1提单对应N货柜)")
public class OceanCounterForm {

    @ApiModelProperty(value = "自增加ID", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "柜号", position = 2)
    @JSONField(ordinal = 2)
    private String cntrNo;

    @ApiModelProperty(value = "柜型(cabinet_type idcode)", position = 3)
    @JSONField(ordinal = 3)
    private String cabinetCode;

    @ApiModelProperty(value = "总体积", position = 4)
    @JSONField(ordinal = 4)
    private Double volume;

    @ApiModelProperty(value = "费用", position = 5)
    @JSONField(ordinal = 5)
    private BigDecimal cost;

    @ApiModelProperty(value = "币种(currency表id)", position = 5)
    @JSONField(ordinal = 6)
    private Integer cid;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 6)
    @JSONField(ordinal = 7)
    private String status;

    @ApiModelProperty(value = "提单id(ocean_bill id)", position = 7)
    @JSONField(ordinal = 8)
    private Long obId;

    @ApiModelProperty(value = "创建时间", position = 8)
    @JSONField(ordinal = 9)
    private LocalDateTime createTime;

    //提单对应货柜信息和客户关联表
    @ApiModelProperty(value = "提单对应货柜信息和客户关联表", position = 9)
    @JSONField(ordinal = 10)
    private List<OceanCounterCustomerRelation> oceanCounterCustomerRelationList;

    //1柜子对应N运单
    @ApiModelProperty(value = "1柜子对应N运单", position = 9)
    @JSONField(ordinal = 11)
    private List<OceanWaybillForm> oceanWaybillFormList;


}
