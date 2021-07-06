package com.jayud.mall.model.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CostItemForm {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "辨认费用类型(1应收费用 2应付费用)")
    private String identifying;

    @ApiModelProperty(value = "费用代码")
    private String costCode;

    @ApiModelProperty(value = "费用名称")
    private String costName;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

    @ApiModelProperty(value = "创建用户id(system_user id)")
    private Integer userId;

    @ApiModelProperty(value = "创建用户名(system_user name)")
    private String userName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "例外的费用代码")
    private List<String> notCostCodes;

}
