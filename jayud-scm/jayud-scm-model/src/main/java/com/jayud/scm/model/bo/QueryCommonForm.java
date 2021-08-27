package com.jayud.scm.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class QueryCommonForm extends BasePageForm{

    @ApiModelProperty("搜索条件")
    private String name;

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("下一步操作")
    private Integer next;

    @ApiModelProperty("车次状态")
    private Integer status;

    @ApiModelProperty("权限code")
    private String actionCode;

    @ApiModelProperty("id集合")
    private List<Integer> ids;

    @ApiModelProperty(value = "封条号")
    private String lockNum;

    @ApiModelProperty(value = "封条颜色")
    private String lockColour;


    //报关日期录入
    @ApiModelProperty(value = "报关日期")
    private String customsDate;

    @ApiModelProperty(value = "报关单号")
    private String customsNo;

    @ApiModelProperty(value = "香港报关单号")
    private String hkBillNo;

}
