package com.jayud.oms.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

/**
 * 小程序订单
 */
public class MiniAppOrderVO {

    @ApiModelProperty(value = "主订单id")
    private Long id;

    @ApiModelProperty(value = "主订单编号")
    private String orderNo;

    @ApiModelProperty(value = "业务员id")
    private Integer bizUid;

    @ApiModelProperty(value = "业务员")
    private String bizUname;



}
