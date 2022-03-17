package com.jayud.wms.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * OrderTrack 实体类
 *
 * @author jyd
 * @since 2021-12-18
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="订单轨迹对象", description="订单轨迹")
@TableName(value = "wms_order_track")
public class OrderTrack extends SysBaseEntity {


    @ApiModelProperty(value = "关联订单编号")
    private String orderNo;

    @ApiModelProperty(value = "子订单号")
    private String subOrderNo;

    @ApiModelProperty(value = "状态码")
    private String status;

    @ApiModelProperty(value = "状态名")
    private String statusName;

    @ApiModelProperty(value = "业务类型(1:出库,2:入库)")
    private Integer type;

    @ApiModelProperty(value = "备注信息")
    private String remark;


    @ApiModelProperty(value = "是否删除")
    //@TableLogic //@TableLogic注解表示逻辑删除,设置修改人手动更新，这里注释
    private Boolean isDeleted;






}
