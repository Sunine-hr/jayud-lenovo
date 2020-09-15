package com.jayud.model.po;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 委托单-随附单证
 *
 * @author william
 * @description
 * @Date: 2020-09-07 18:24
 */
@Data
public class CustomsAppendix {
    @ApiModelProperty(value = "序号")
    private Integer num_no;
    @ApiModelProperty(value = "随附单证编号代码")
    private String control_no;
    @ApiModelProperty(value = "随附单证编号")
    private String accompany_no;
}
