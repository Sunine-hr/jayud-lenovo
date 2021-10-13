package com.jayud.scm.model.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryCustomerForm extends BasePageForm{

    @ApiModelProperty("搜索条件")
    private String condition;

    @ApiModelProperty("搜索key")
    private String key;

    @ApiModelProperty("业务类型")
    private String businessType;

    @ApiModelProperty("客户类型")
    private String type;

    @ApiModelProperty("类型")
    private String classType;

    @ApiModelProperty("客户名称")
    private String customerName;

//    @ApiModelProperty("是否归类")
//    private String isClassification;

    @ApiModelProperty("审核状态 全部、未审核、已审核 N1 N2 N3")
    private String auditStatus;

    @ApiModelProperty("创建日期")
    private String[] createTime;

    @ApiModelProperty("开始时间")
    @JsonIgnore
    private String startTime;

    @ApiModelProperty("结束时间")
    @JsonIgnore
    private String endTime;

    public void setTime(){
        if(createTime != null && createTime.length>0){
            startTime = createTime[0];
            endTime = createTime[1];
        }
    }


}
