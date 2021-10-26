package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 月台管理
 * </p>
 *
 * @author CYC
 * @since 2021-10-23
 */
@Data
public class QueryPlatformForm extends BasePageForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "月台名称")
    private String platformName;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;
}
