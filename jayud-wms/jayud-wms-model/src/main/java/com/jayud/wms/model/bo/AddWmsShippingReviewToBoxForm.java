package com.jayud.wms.model.bo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * WmsShippingReviewToBox 实体类
 *
 * @author jyd
 * @since 2021-12-24
 */
@Data
public class AddWmsShippingReviewToBoxForm extends SysBaseEntity {


    @ApiModelProperty(value = "出库单号")
    private String orderNumber;

    @ApiModelProperty(value = "拣货下架单id")
    private Long packingOffshelfId;

    @ApiModelProperty(value = "拣货下架单号")
    private String packingOffshelfNumber;

    @ApiModelProperty(value = "仓库id")
    private Long warehouseId;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "货主id")
    private Long owerId;

    @ApiModelProperty(value = "货主编码")
    private String owerCode;

    @ApiModelProperty(value = "货主名称")
    private String owerName;

    @ApiModelProperty(value = "物料id")
    private Long materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "拣货数量")
    private Integer packingAccount;

    @ApiModelProperty(value = "箱编号")
    private Integer boxNumber;

    @ApiModelProperty(value = "数量")
    private Integer account;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic //@TableLogic注解表示逻辑删除,设置修改人手动更新，这里注释
    private Boolean isDeleted;






}
