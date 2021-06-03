package com.jayud.storage.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 上架订单商品列表查询条件
 */

@Data
public class QueryPutGoodForm extends BasePageForm{

    @ApiModelProperty(value = "入库批次号")
    private String warehousingBatchNo;

    @ApiModelProperty(value = "搜索时间")
    private String searchTime;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "创建时间")
    private String[] createTime;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    public void setStartTime(){
        String[] time = this.createTime;
        if(time != null && time.length>0){
            this.startTime = time[0];
            this.endTime = time[1];
        }
    }
}
