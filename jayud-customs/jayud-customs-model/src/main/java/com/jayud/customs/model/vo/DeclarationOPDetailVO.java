package com.jayud.customs.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 报关进程VO-detail
 *
 * @author william
 * @description
 * @Date: 2020-09-09 17:19
 */
@Data
public class DeclarationOPDetailVO {
    @ApiModelProperty(value = "进程ID")
    private String bg_state_id;
    @ApiModelProperty(value = "进程名称")
    private String cname;
    @ApiModelProperty(value = "进程时间")
    private String process_dt;
    @ApiModelProperty(value = "操作人")
    private String p_name;
    @ApiModelProperty(value = "操作人手机")
    private String a_phone;
    @ApiModelProperty(value = "操作人代码")
    private String zucode;
}
