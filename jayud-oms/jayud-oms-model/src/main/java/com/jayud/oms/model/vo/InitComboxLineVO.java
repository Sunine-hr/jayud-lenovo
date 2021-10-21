package com.jayud.oms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 线路管理
 * </p>
 *
 * @author CYC
 * @since 2021-10-20
 */
@Data
public class InitComboxLineVO {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "线路名称")
    private String lineName;
}
