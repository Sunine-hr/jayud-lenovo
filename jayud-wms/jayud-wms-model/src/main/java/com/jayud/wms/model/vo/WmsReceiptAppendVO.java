package com.jayud.wms.model.vo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * WmsReceiptAppend 实体类
 *
 * @author jayud
 * @since 2022-03-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="收货单附加服务表对象", description="收货单附加服务表")
public class WmsReceiptAppendVO extends SysBaseEntity {


    @ApiModelProperty(value = "收货单id")
    private Long orderId;

    @ApiModelProperty(value = "收货单号")
    private String orderNum;

    @ApiModelProperty(value = "服务名称")
    private String theServiceName;

    @ApiModelProperty(value = "数量")
    private Double num;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "总价")
    private Double totalPrices;

    @ApiModelProperty(value = "单价")
    private Double unitPrice;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注信息")
    private String remark;


    @ApiModelProperty(value = "是否删除")
    @TableLogic
    private Boolean isDeleted;





}
