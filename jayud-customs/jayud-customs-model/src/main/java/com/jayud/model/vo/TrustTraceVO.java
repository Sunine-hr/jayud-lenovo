package com.jayud.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 委托进程字段VO
 *
 * @author william
 * @description
 * @Date: 2020-09-09 17:59
 */
@Data
public class TrustTraceVO {
    @ApiModelProperty(value = "委托创建时间")
    private LocalDateTime created_dt ;

    @ApiModelProperty(value = "委托发送时间")
    private LocalDateTime FDt ;

    @ApiModelProperty(value = "委托接受时间")
    private LocalDateTime SDt ;

    @ApiModelProperty(value = "委托接受单位")
   private String s_org_name ;

    @ApiModelProperty(value = "委托状态")
    private String  state ;

    @ApiModelProperty(value = "创建人")
    private String c_name ;

    @ApiModelProperty(value = "发送人")
    private String f_name ;

    @ApiModelProperty(value = "接受人")
    private String s_name ;

}
