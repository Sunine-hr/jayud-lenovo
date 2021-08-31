package com.jayud.scm.model.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class
QueryCommodityForm extends BasePageForm{

    @ApiModelProperty("搜索条件")
    private String condition;

    @ApiModelProperty("搜索key")
    private String key;

    @ApiModelProperty("创建日期")
    private String[] createTime;

    @ApiModelProperty("开始时间")
    @JsonIgnore
    private String startTime;

    @ApiModelProperty("结束时间")
    @JsonIgnore
    private String endTime;

    @ApiModelProperty("是否归类")
    private Boolean classify;

//    @ApiModelProperty("是否归类")
//    private String isClassification;

    @ApiModelProperty("审核状态 0 , 1 ,2")
    private String auditStatus;

    public void setTime(){
        if(createTime != null && createTime.length>0){
            startTime = createTime[0]+" 00:00:00";
            endTime = createTime[1]+" 23:59:59";
        }
    }

}
