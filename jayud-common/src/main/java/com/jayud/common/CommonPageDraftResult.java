package com.jayud.common;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.Map;

@Getter
public class CommonPageDraftResult<T> extends CommonPageResult<T>{

    //分页时，显示分页信息和统计的草稿数量
    @ApiModelProperty(value = "草稿数量")
    private Long draftNum;
    @ApiModelProperty(value = "已下单数量")
    private Long orderedNum;
    @ApiModelProperty(value = "已收货数量")
    private Long receivedNum;
    @ApiModelProperty(value = "订单确认数量")
    private Long affirNum;
    @ApiModelProperty(value = "转运中数量")
    private Long transitNum;
    @ApiModelProperty(value = "已签收数量")
    private Long signedNum;

    public CommonPageDraftResult(IPage pageInfo, Map<String, Long> totalMap) {
        super(pageInfo);
        Long draftNum = MapUtil.getLong(totalMap, "draftNum");
        Long orderedNum = MapUtil.getLong(totalMap, "orderedNum");
        Long receivedNum = MapUtil.getLong(totalMap, "receivedNum");
        Long affirNum = MapUtil.getLong(totalMap, "affirNum");
        Long transitNum = MapUtil.getLong(totalMap, "transitNum");
        Long signedNum = MapUtil.getLong(totalMap, "signedNum");
        this.draftNum = draftNum;
        this.orderedNum = orderedNum;
        this.receivedNum = receivedNum;
        this.affirNum = affirNum;
        this.transitNum = transitNum;
        this.signedNum = signedNum;
    }
}
