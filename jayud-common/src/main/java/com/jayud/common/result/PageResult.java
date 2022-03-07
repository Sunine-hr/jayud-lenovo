package com.jayud.common.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ciro
 * @date 2021/12/15 17:02
 * @description:    分页数据
 */
@Data
@ApiModel(value="分页详情", description="分页详情")
public class PageResult {

    @ApiModelProperty(value = "总数")
    private long total;

    @ApiModelProperty(value = "每页大小")
    private long size;

    @ApiModelProperty(value = "当前页数")
    private long current;

    @ApiModelProperty(value = "总页数")
    private long pages;


}
