package com.jayud.common.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ciro
 * @date 2021/12/15 17:00
 * @description:    分页结果响应
 */
@Data
@ApiModel(value="分页结果响应", description="分页结果响应")
public class ListPageRuslt<T> {

    @ApiModelProperty(value = "分页数据")
    private T records;

    @ApiModelProperty(value = "分页详情")
    private PageResult pagination;
}
