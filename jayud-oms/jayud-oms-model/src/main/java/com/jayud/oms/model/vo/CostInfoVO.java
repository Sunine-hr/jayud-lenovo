package com.jayud.oms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

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

    @ApiModelProperty(value = "费用名状态(0禁用 1启用)")
    private String status;

    @ApiModelProperty(value = "费用类别")
    private List<Long> cids;

    @ApiModelProperty(value = "描述")
    private String remarks;

    @ApiModelProperty(value = "费用类型")
    private String codeName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;


}
