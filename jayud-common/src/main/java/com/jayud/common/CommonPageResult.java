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
    @ApiModelProperty("页码")
    private Long pageNum;
    @ApiModelProperty("页长")
    private Long pageSize;
    @ApiModelProperty("总记录数")
    private Long total;
    @ApiModelProperty("是否有下一页")
    private Boolean hasNextPage;
    @ApiModelProperty("列表内容")
    private List<T> list;
    @ApiModelProperty("总页数")
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