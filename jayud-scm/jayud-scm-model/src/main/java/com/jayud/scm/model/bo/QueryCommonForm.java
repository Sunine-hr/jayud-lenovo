package com.jayud.scm.model.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
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


    //入库
    @ApiModelProperty(value = "仓库名称")
    private String hubName;

    @ApiModelProperty(value = "入库日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime receivingDate;

    @ApiModelProperty(value = "储位")
    private String storage;

    @ApiModelProperty(value = "标记（0：否   1：是）")
    private Integer sign;

    @ApiModelProperty(value = "到货方式")
    private String deliveryMode;

    @ApiModelProperty(value = "备注")
    private String remark;


    //水单管理
    @ApiModelProperty(value = "实际到帐金额")
    private BigDecimal billArMoney;

    @ApiModelProperty(value = "到账日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private String accountDate;

    //收款单管理
    @ApiModelProperty(value = "锁定金额")
    private BigDecimal lockMoney;

    @ApiModelProperty(value = "原因")
    private String reason;

    @ApiModelProperty(value = "出口结汇汇率")
    private BigDecimal accRate;


}
