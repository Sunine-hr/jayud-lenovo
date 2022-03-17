package com.jayud.common.dto;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * WmsShippingReview 实体类
 *
 * @author jyd
 * @since 2021-12-24
 */
@Data
public class WmsShippingReviewForm extends SysBaseEntity {


    @ApiModelProperty(value = "波次单号")
    private String wareNumber;

    @ApiModelProperty(value = "出库单号")
    private String orderNumber;

    @ApiModelProperty(value = "是否完成(0否，1是，2关箱)")
    private Integer isEnd;

    @ApiModelProperty(value = "拣货下架单id")
    private Long packingOffshelfId;

    @ApiModelProperty(value = "拣货下架单号")
    private String packingOffshelfNumber;

    @ApiModelProperty(value = "仓库id")
    private Long warehouseId;

    @ApiModelProperty(value = "货主id")
    private Long owerId;

    @ApiModelProperty(value = "物料id")
    private Long materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "拣货数量")
    private Integer packingAccount;

    @ApiModelProperty(value = "已扫描数量")
    private Integer scannedAccount;

    @ApiModelProperty(value = "未扫描数量")
    private Integer notScannedAccount;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "复核人")
    private String reviewerBy;

    @ApiModelProperty(value = "复核时间")
    private LocalDateTime reviewerTime;

    @ApiModelProperty(value = "批次号")
    private String batchNumber;

    @ApiModelProperty(value = "生产时间")
    private LocalDateTime manufactureTime;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "箱编号")
    private Integer boxNumber;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic //@TableLogic注解表示逻辑删除,设置修改人手动更新，这里注释
    private Boolean isDeleted;






}
