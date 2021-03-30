package com.jayud.tms.model.vo.statistical;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@Accessors(chain = true)
public class ProcessNode {

    @ApiModelProperty(value = "节点名称")
    private String name;

    @ApiModelProperty(value = "完成时间")
    private String completeTime;

    @ApiModelProperty(value = "操作用户")
    private String userName;

    @ApiModelProperty(value = "0:未完成,1:异常,2:完成")
    private Integer status = 0;

}
