package com.jayud.auth.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SystemSqlConfigVO {

    @ApiModelProperty(value = "自动ID")
    private Integer id;

    @ApiModelProperty(value = "SQL代码")
    private String sqlCode;

    @ApiModelProperty(value = "SQL名称")
    private String sqlName;

    @ApiModelProperty(value = "查询语句")
    private String sqlStr;

    @ApiModelProperty(value = "查询参数(可默认包含@WHERE)")
    private String sqlParams;

    @ApiModelProperty(value = "查询排序(@ORDER)")
    private String sqlOrder;

    @ApiModelProperty(value = "是否含有查询条件(1是 0否)")
    private Integer isHavParam;

    @ApiModelProperty(value = "是否允许带查询条件(1是 0否)")
    private Integer isCondition;

    @ApiModelProperty(value = "无数据时返回值")
    private String msgStr;

    @ApiModelProperty(value = "查询列显示（字段名：显示名：宽度：格式等，）")
    private String columnName;

    @ApiModelProperty(value = "数据权限查询条件")
    private String sqlDataStr;

    @ApiModelProperty(value = "数据汇总列格式（列名：格式，）")
    private String sqlSumColumn;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人ID(system_user id)")
    private Integer crtBy;

    @ApiModelProperty(value = "创建人名称(system_user user_name)")
    private String crtByName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime crtByDtm;

    @ApiModelProperty(value = "最后修改人ID")
    private Integer mdyBy;

    @ApiModelProperty(value = "最后修改人名称")
    private String mdyByName;

    @ApiModelProperty(value = "最后修改时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime mdyByDtm;

    @ApiModelProperty(value = "删除标记")
    private Integer voided;

    @ApiModelProperty(value = "删除人ID")
    private Integer voidedBy;

    @ApiModelProperty(value = "删除人名称")
    private String voidedByName;

    @ApiModelProperty(value = "删除时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime voidedByDtm;

    @ApiModelProperty(value = "版本号")
    private Long version;


}
