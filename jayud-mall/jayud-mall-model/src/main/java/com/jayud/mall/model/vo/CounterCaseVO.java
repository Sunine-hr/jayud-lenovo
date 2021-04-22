package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ApiModel(value = "CounterCaseVO", description = "提单柜号，关联运单箱号")
@Data
public class CounterCaseVO {

    @ApiModelProperty(value = "主键id", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "提单柜号id(ocean_counter id)", position = 2)
    @JSONField(ordinal = 2)
    private Long oceanCounterId;

    @ApiModelProperty(value = "运单箱号id[订单箱号id](order_case id)", position = 3)
    @JSONField(ordinal = 3)
    private Long orderCaseId;

    @ApiModelProperty(value = "创建用户id(system_user id)", position = 4)
    @JSONField(ordinal = 4)
    private Integer userId;

    @ApiModelProperty(value = "创建用户名(system_user name)", position = 5)
    @JSONField(ordinal = 5)
    private String userName;

    @ApiModelProperty(value = "创建时间", position = 6)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 6, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /*装柜信息*/

    //柜号
    @ApiModelProperty(value = "柜号", position = 7)
    @JSONField(ordinal = 7)
    private String cntrNo;

    //提单id
    @ApiModelProperty(value = "提单id", position = 8)
    @JSONField(ordinal = 8)
    private Long tdId;

    //提单号
    @ApiModelProperty(value = "提单号", position = 9)
    @JSONField(ordinal = 9)
    private String tdh;

    //箱号
    @ApiModelProperty(value = "箱号", position = 10)
    @JSONField(ordinal = 10)
    private String cartonNo;

    //体积，最终确认体积
    @ApiModelProperty(value = "体积，最终确认体积", position = 11)
    @JSONField(ordinal = 11)
    private BigDecimal confirmVolume;

    //订单号
    @ApiModelProperty(value = "订单号", position = 12)
    @JSONField(ordinal = 12)
    private String orderNo;

}
