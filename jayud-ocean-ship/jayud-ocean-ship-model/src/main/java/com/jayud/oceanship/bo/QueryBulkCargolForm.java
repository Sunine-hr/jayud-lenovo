package com.jayud.oceanship.bo;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 海运订单列表
 * </p>
 *
 * @author
 * @since
 */
@Data
public class QueryBulkCargolForm extends BasePageForm {

    @ApiModelProperty(value = "提单编号")
    private String orderNo;

    @ApiModelProperty(value = "出发港口")
    private String portDeparture;

    @ApiModelProperty(value = "目的地港口")
    private String portDestination;

    @ApiModelProperty(value = "截补料时间")
    private String[] cutReplenishTime;

    @ApiModelProperty(value = "开始截补料时间")
    private String startCutReplenishTime;

    @ApiModelProperty(value = "结束截补料时间")
    private String endCutReplenishTime;

    public void setStartTime(){
        String[] time = this.cutReplenishTime;
        if(time != null && time.length>0){
            this.startCutReplenishTime = time[0];
            this.endCutReplenishTime = time[1];
        }
    }
}
