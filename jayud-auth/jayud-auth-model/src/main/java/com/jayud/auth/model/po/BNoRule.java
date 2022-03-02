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
 * BNoRule 实体类
 *
 * @author jayud
 * @since 2022-03-02
 * @since 2022-03-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="编号规则表对象", description="编号规则表")
public class BNoRule extends SysBaseEntity {


    @ApiModelProperty(value = "类型编码(唯一)")
    private String noCode;

    @ApiModelProperty(value = "编号类型说明（哪个模块）")
    private String noDesc;

    @ApiModelProperty(value = "前缀")
    private String noPrefix;

    @ApiModelProperty(value = "格式年月等（如：年月yyMM）")
    private String noFormat;

    @ApiModelProperty(value = "审核级别")
    private Integer checkLength;

    @ApiModelProperty(value = "审核表")
    private String checkTable;

    @ApiModelProperty(value = "两次审核能否为同一个人")
    private Boolean check;

    @ApiModelProperty(value = "审核库")
    private String checkDatabase;

    @ApiModelProperty(value = "流水号长度")
    private Integer tNum;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人ID")
    private Integer crtBy;

    @ApiModelProperty(value = "创建人名称")
    private String crtByName;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime crtByDtm;

    @ApiModelProperty(value = "最后修改人ID")
    private Integer mdyBy;

    @ApiModelProperty(value = "最后修改人名称")
    private String mdyByName;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "最后修改时间")
    private LocalDateTime mdyByDtm;

    @ApiModelProperty(value = "删除标记")
    private Integer voided;

    @ApiModelProperty(value = "删除人ID")
    private Integer voidedBy;

    @ApiModelProperty(value = "删除人名称")
    private String voidedByName;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "删除时间")
    private LocalDateTime voidedByDtm;
    private Integer isDelete;

    @ApiModelProperty(value = "两次审核能否为同一个人")
    private Boolean check;


}
