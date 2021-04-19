package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OceanCounterCaseVO {

    @ApiModelProperty(value = "装柜信息（提单关联装柜信息  提单  货柜  运单箱号）", position = 1)
    @JSONField(ordinal = 1)
    private List<CounterCaseVO> counterCaseVOS;

    @ApiModelProperty(value = "装柜信息体积合计", position = 2)
    @JSONField(ordinal = 2)
    private BigDecimal counterCaseVolumeTotal;

}
