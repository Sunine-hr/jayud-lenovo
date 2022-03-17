package com.jayud.wms.model.po;

import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * WmsPackingOffshelfOrder 实体类
 *
 * @author jyd
 * @since 2022-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="拣货下架单对象", description="拣货下架单")
public class WmsPackingOffshelfOrder extends SysBaseEntity {


    @ApiModelProperty(value = "拣货下架单号")
    private String packingOffshelfNumber;

    @ApiModelProperty(value = "出库单号")
    private String orderNumber;

    @ApiModelProperty(value = "波次号")
    private String waveNumber;

    @ApiModelProperty(value = "明细总数")
    private Integer allDetailCount;

    @ApiModelProperty(value = "完成下架明细数量")
    private Integer finishDetailCount;

    @ApiModelProperty(value = "未完成下架明细数量")
    private Integer unfinishDetailCount;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    //@TableLogic //@TableLogic注解表示逻辑删除,设置修改人手动更新，这里注释
    private Boolean isDeleted;






    }
