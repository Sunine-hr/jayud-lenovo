package com.jayud.common;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.Map;

@Getter
public class CommonPageDraftResult<T> extends CommonPageResult<T>{

    //分页时，显示分页信息和统计数量
    @ApiModelProperty(value = "草稿")
    private Long draftNum;
    @ApiModelProperty(value = "补资料")
    private Long updateNum;
    @ApiModelProperty(value = "已下单")
    private Long placedNum;
    @ApiModelProperty(value = "已收货")
    private Long receivedNum;
    @ApiModelProperty(value = "转运中")
    private Long transitNum;
    @ApiModelProperty(value = "已签收")
    private Long signedNum;
    @ApiModelProperty(value = "已完成")
    private Long finishNum;
    @ApiModelProperty(value = "已取消")
    private Long cancelNum;

    public CommonPageDraftResult(IPage pageInfo, Map<String, Long> totalMap) {
        super(pageInfo);
        Long draftNum = MapUtil.getLong(totalMap, "draftNum");
        Long updateNum = MapUtil.getLong(totalMap, "updateNum");
        Long placedNum = MapUtil.getLong(totalMap, "placedNum");
        Long receivedNum = MapUtil.getLong(totalMap, "receivedNum");
        Long transitNum = MapUtil.getLong(totalMap, "transitNum");
        Long signedNum = MapUtil.getLong(totalMap, "signedNum");
        Long finishNum = MapUtil.getLong(totalMap, "finishNum");
        Long cancelNum = MapUtil.getLong(totalMap, "cancelNum");
        this.draftNum = draftNum;
        this.updateNum = updateNum;
        this.placedNum = placedNum;
        this.receivedNum = receivedNum;
        this.transitNum = transitNum;
        this.signedNum = signedNum;
        this.finishNum = finishNum;
        this.cancelNum = cancelNum;
    }
}
