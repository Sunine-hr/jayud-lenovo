package com.jayud.auth.model.po;

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
 * BPublicCheck 实体类
 *
 * @author jayud
 * @since 2022-02-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="审核记录表对象", description="审核记录表")
public class BPublicCheck extends SysBaseEntity {


    @ApiModelProperty(value = "表名")
    private String sheetCode;

    @ApiModelProperty(value = "操作,1审核，0反审")
    private Integer checkFlag;

    @ApiModelProperty(value = "记录ID")
    private Long recordId;

    @ApiModelProperty(value = "需要审核级别")
    private Integer fLevel;

    @ApiModelProperty(value = "当前审核级别")
    private Integer fStep;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "审核人ID")
    private Long fCheckId;

    @ApiModelProperty(value = "审核人名称")
    private String fCheckName;


}
