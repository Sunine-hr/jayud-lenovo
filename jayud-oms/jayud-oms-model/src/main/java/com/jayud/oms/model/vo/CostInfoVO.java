package com.jayud.oms.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @ApiModelProperty(value = "是否展示给司机")
    private Boolean isDriverShow;

    @ApiModelProperty(value = "是否实报实销")
    private Boolean isReimbursement;


}
