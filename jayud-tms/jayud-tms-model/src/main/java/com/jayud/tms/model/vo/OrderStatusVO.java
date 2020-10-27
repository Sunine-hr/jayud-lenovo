package com.jayud.tms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class OrderStatusVO {

    private long id;

    @ApiModelProperty(value = "流程CODE")
    private String processCode;

    @ApiModelProperty(value = "流程名称")
    private String processName;

    @ApiModelProperty(value = "流程状态 1-灰色 2-进行中 3-已完成 4-异常")
    private String status;

    @ApiModelProperty(value = "流程状态变更时间")
    private String statusChangeTime;

    @ApiModelProperty(value = "子流程")
    private List<OrderStatusVO> children = new ArrayList<>();

    public void setStatus() {
        if(status == null){
            this.status = "1";
        }
    }

    public void addChildren(OrderStatusVO vo) {
        children.add(vo);
    }


}
