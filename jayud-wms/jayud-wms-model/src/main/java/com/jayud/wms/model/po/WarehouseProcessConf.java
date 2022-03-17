package com.jayud.wms.model.po;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * WarehouseProcessConf 实体类
 *
 * @author jyd
 * @since 2021-12-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="WarehouseProcessConf对象", description="仓库流程配置")
@TableName(value = "wms_warehouse_process_conf")
public class WarehouseProcessConf extends SysBaseEntity {


    @ApiModelProperty(value = "所属仓库id")
    private Long warehouseId;

    @ApiModelProperty(value = "类型(1:入库,2:出库)")
    private Integer type;

    @ApiModelProperty(value = "流程编码")
    private String code;

    @ApiModelProperty(value = "流程名称")
    private String name;

    @ApiModelProperty(value = "是否执行")
    private Boolean isExecute;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注信息")
    private String remark;


    @ApiModelProperty(value = "是否删除")
    @TableLogic
    private Boolean isDeleted;






}
