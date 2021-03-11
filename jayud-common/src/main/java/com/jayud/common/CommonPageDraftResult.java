package com.jayud.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class CommonPageDraftResult<T> extends CommonPageResult<T>{

    //分页时，显示分页信息和统计的草稿数量
    @ApiModelProperty(value = "草稿数量", position = 1)
    private Long draftNum;

    public CommonPageDraftResult(IPage pageInfo, Long draftNum) {
        super(pageInfo);
        this.draftNum = draftNum;
    }
}
