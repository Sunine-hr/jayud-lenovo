package com.jayud.oms.order.model.po;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

/**
 * OmsOrderFollow 实体类
 *
 * @author jayud
 * @since 2022-03-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="订单状态跟进表对象", description="订单状态跟进表")
public class OmsOrderFollow extends SysBaseEntity {


    @ApiModelProperty(value = "订单主表ID")
    private Long omsOrderId;

    @ApiModelProperty(value = "状态类型")
    private String sType;

    @ApiModelProperty(value = "状态信息")
    private String followContext;

    @ApiModelProperty(value = "状态时间")
    private String followTime;

    @ApiModelProperty(value = "是否同步在线")
    private Boolean ifOnl;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "备注")
    private LocalDateTime remark;

    @ApiModelProperty(value = "组织机构ID")
    private Long orgId;

    @ApiModelProperty(value = "多租户ID")
    private Long tenantId;






    @ApiModelProperty(value = "删除标志")
    @TableLogic
    private Boolean isDeleted;
    @ApiModelProperty(value = "删除人")
    private Long deletedUserId;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "删除时间")
    private LocalDateTime deleteUserName;


}
