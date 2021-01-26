package com.jayud.oms.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 基础数据费用类型
 * </p>
 *
 * @author 李达荣
 * @since 2020-10-30
 */
@Data
public class CostGenreVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "费用类型代码")
    private String code;

    @ApiModelProperty(value = "费用类型名称")
    private String name;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "描述")
    private String remarks;

    @ApiModelProperty(value = "状态（1启用，0禁用）")
    private String status;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;


}
