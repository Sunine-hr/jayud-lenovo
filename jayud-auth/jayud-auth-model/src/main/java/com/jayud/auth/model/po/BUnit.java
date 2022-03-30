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
 * BUnit 实体类
 *
 * @author jayud
 * @since 2022-03-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="计量单位代码表对象", description="计量单位代码表")
public class BUnit extends SysBaseEntity {


    @ApiModelProperty(value = "单位CODE")
    private String unitCode;

    @ApiModelProperty(value = "中文名称")
    private String unitNameCh;

    @ApiModelProperty(value = "英文名称")
    private String unitNameEn;

    @ApiModelProperty(value = "备注")
    private String remark;






    @ApiModelProperty(value = "删除标记")
    @TableLogic
    private Boolean isDeleted;

}
