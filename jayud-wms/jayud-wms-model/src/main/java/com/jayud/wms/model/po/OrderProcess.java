package com.jayud.wms.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * OrderProcess 实体类
 *
 * @author jyd
 * @since 2021-12-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "OrderProcess对象", description = "订单流程")
@TableName(value = "wms_order_process")
public class OrderProcess {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键ID")
    protected Long id;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "流程code")
    private String status;

    @ApiModelProperty(value = "流程名")
    private String statusName;

    @ApiModelProperty(value = "上一流程节点code")
    private String fStatus;

    @ApiModelProperty(value = "类型(1:入库,2:出库)")
    private Integer type;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date createTime;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    protected String createBy;

}
