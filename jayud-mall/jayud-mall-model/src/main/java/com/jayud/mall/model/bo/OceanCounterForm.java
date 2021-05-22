package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    @ApiModelProperty(value = "柜型(cabinet_type id_code)", position = 3)
    @JSONField(ordinal = 3)
    private String cabinetCode;

    @ApiModelProperty(value = "总体积", position = 4)
    @JSONField(ordinal = 4)
    private Double volume;

    @ApiModelProperty(value = "费用", position = 5)
    @JSONField(ordinal = 5)
    private BigDecimal cost;

    @ApiModelProperty(value = "币种(currency_info id)", position = 6)
    @JSONField(ordinal = 6)
    private Integer cid;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 7)
    @TableField(value = "`status`")
    @JSONField(ordinal = 7)
    private String status;

    @ApiModelProperty(value = "提单id(ocean_bill id)", position = 8)
    @JSONField(ordinal = 8)
    private Long obId;

    @ApiModelProperty(value = "创建时间", position = 9)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 9, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "费用信息")
    private List<FeeCopeWithForm> feeCopeWithList;



}
