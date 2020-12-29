package com.jayud.finance.bo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class ListForm {

    @ApiModelProperty(value = "批量集合ID")
    private List<Long> ids;

    @ApiModelProperty(value = "账单编号集合")
    private List<String> billNos;

    @ApiModelProperty(value = "操作指令 cmd=kf_f_reject客服反审核 cw_f_reject财务反审核")
    private String cmd;

    @ApiModelProperty(value = "当前登录用户")
    private String loginUserName;

    @ApiModelProperty(value = "备注")
    private String remark;


}
