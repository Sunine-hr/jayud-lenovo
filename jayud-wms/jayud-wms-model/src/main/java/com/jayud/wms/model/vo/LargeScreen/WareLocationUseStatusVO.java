package com.jayud.wms.model.vo.LargeScreen;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

/**
 * @author ciro
 * @date 2022/4/11 10:49
 * @description:
 */
@Data
@ApiModel(value = "库位使用状态", description = "库位使用状态")
public class WareLocationUseStatusVO {


    @ApiModelProperty(value = "使用数量")
    private Integer useCount;

    @ApiModelProperty(value = "未使用数量")
    private Integer unUseCount;

}
