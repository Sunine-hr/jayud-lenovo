package com.jayud.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.List;

/**
 * 公共分页结果
 * @param <T>
 */
@Getter
public class CommonPageResult<T> {
    @ApiModelProperty(value = "页码", position = 1)
    private Long pageNum;
    @ApiModelProperty(value = "页长", position = 2)
    private Long pageSize;
    @ApiModelProperty(value = "总记录数", position = 3)
    private Long total;
    @ApiModelProperty(value = "是否有下一页", position = 4)
    private Boolean hasNextPage;
    @ApiModelProperty(value = "列表内容", position = 5)
    private List<T> list;
    @ApiModelProperty(value = "总页数", position = 6)
    private Long totalPages;

    public CommonPageResult(IPage pageInfo) {
        this.pageNum = pageInfo.getCurrent();
        this.pageSize = pageInfo.getSize();
        this.total = pageInfo.getTotal();
        this.hasNextPage = pageInfo.getCurrent() < pageInfo.getPages();
        this.list = pageInfo.getRecords();
        this.totalPages = pageInfo.getPages();
    }

}