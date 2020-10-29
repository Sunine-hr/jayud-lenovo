package com.jayud.oms.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 费用名描述
 * </p>
 *
 * @author 李达荣
 * @since 2020-10-27
 */
@Data
public class CostInfoVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "费用code")
    private String idCode;

    @ApiModelProperty(value = "费用名")
    private String name;

    @ApiModelProperty(value = "费用名状态(1启用 2无效)")
    private String status;

    @ApiModelProperty(value = "描述")
    private String remarks;

    @ApiModelProperty(value = "费用类型")
    private String codeName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime upTime;

    @ApiModelProperty(value = "更新人")
    private String upUser;


}
