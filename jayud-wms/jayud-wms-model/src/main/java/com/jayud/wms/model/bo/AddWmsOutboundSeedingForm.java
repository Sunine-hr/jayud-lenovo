package com.jayud.wms.model.bo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * WmsOutboundSeeding 实体类
 *
 * @author jyd
 * @since 2021-12-24
 */
@Data
public class AddWmsOutboundSeedingForm extends SysBaseEntity {


    @ApiModelProperty(value = "播种位id")
    private Long sowingId;

    @ApiModelProperty(value = "播种位编码")
    private String sowingCode;

    @ApiModelProperty(value = "播种位名称")
    private String sowingName;

    @ApiModelProperty(value = "物料id")
    private Long materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "旧-容器id")
    private Long oldContainerId;

    @ApiModelProperty(value = "旧-容器编码")
    private String oldContainerCode;

    @ApiModelProperty(value = "旧-容器名称")
    private String oldContainerName;

    @ApiModelProperty(value = "旧-数量")
    private Integer oldAccount;

    @ApiModelProperty(value = "旧-播种数量")
    private Integer oldSeedingAccount;

    @ApiModelProperty(value = "旧-播种后数量")
    private Integer oldRealSeedingAccount;

    @ApiModelProperty(value = "旧-单位")
    private String oldUnit;

    @ApiModelProperty(value = "新-容器id")
    private Long newContainerId;

    @ApiModelProperty(value = "新-容器编码")
    private String newContainerCode;

    @ApiModelProperty(value = "新-容器名称")
    private String newContainerName;

    @ApiModelProperty(value = "新-数量")
    private Integer newAccount;

    @ApiModelProperty(value = "新-播种数量")
    private Integer newSeedingAccount;

    @ApiModelProperty(value = "新-播种后数量")
    private Integer newRealSeedingAccount;

    @ApiModelProperty(value = "新-单位")
    private String newUnit;

    @ApiModelProperty(value = "波次单id")
    private Long waveId;

    @ApiModelProperty(value = "波次号")
    private String waveNumber;

    @ApiModelProperty(value = "出库单id")
    private Long orderId;

    @ApiModelProperty(value = "出库单号")
    private String orderNumber;

    @ApiModelProperty(value = "拣货下架单id")
    private Long packingOffshelfId;

    @ApiModelProperty(value = "拣货下架单号")
    private String packingOffshelfNumber;

    @ApiModelProperty(value = "出库通知单id")
    private Long noticeId;

    @ApiModelProperty(value = "出库通知单号")
    private Long noticeNumber;

    @ApiModelProperty(value = "分配物料明细id")
    private Long allocationId;

    @ApiModelProperty(value = "状态(0-未确认，1-已确认)")
    private Integer status;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic //@TableLogic注解表示逻辑删除,设置修改人手动更新，这里注释
    private Boolean isDeleted;






}
