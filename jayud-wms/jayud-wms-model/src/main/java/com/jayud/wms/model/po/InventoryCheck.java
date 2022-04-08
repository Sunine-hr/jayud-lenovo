package com.jayud.wms.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * InventoryCheck 实体类
 *
 * @author jyd
 * @since 2021-12-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="库存盘点表对象", description="库存盘点表")
@TableName(value = "wms_inventory_check")
public class InventoryCheck extends SysBaseEntity {


    @ApiModelProperty(value = "盘点单号")
    private String checkCode;

    @ApiModelProperty(value = "仓库ID")
    private Long warehouseId;

    @ApiModelProperty(value = "仓库编号")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "盘点状态(1未盘点、2部分盘点、3已盘点)")
    private Integer checkStatus;

    @ApiModelProperty(value = "盘点类型(1明盘(显示账面数量) 2盲盘(不显示账面数量))")
    private Integer checkType;

    @ApiModelProperty(value = "库存数量")
    private BigDecimal inventoryCount;

    @ApiModelProperty(value = "盘点数量")
    private BigDecimal checkCount;

    @ApiModelProperty(value = "盘点人")
    private String checkBy;

    @ApiModelProperty(value = "盘点开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkStartTime;

    @ApiModelProperty(value = "盘点完成时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkFinishTime;

    @ApiModelProperty(value = "更新开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateStartTime;

    @ApiModelProperty(value = "更新完成时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateFinishTime;

    @ApiModelProperty(value = "备注信息")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    //@TableLogic //@TableLogic注解表示逻辑删除,设置修改人手动更新，这里注释
    private Boolean isDeleted;

    /************************* 惠州道科-业务 新增字段 @MFC 2022年3月5日09:41:49 *************************************/
    @ApiModelProperty(value = "工作台id")
    private Long workbenchId;

    /************************* 惠州道科-业务 新增字段 @MFC 2022年3月5日09:41:49 *************************************/
    @ApiModelProperty(value = "工作台编号")
    private String workbenchCode;

    /************************* 惠州道科-业务 新增字段 @MFC 2022年3月5日09:41:49 *************************************/
    @ApiModelProperty(value = "货架id")
    private Long shelfId;

    /************************* 惠州道科-业务 新增字段 @MFC 2022年3月5日09:41:49 *************************************/
    @ApiModelProperty(value = "货架编号")
    private String shelfCode;

    //公司
    @TableField(exist = false)
    private List<String> orgIds;

    //是否在这个体系内
    @TableField(exist = false)
    private Boolean isCharge;

    //货主id
    @TableField(exist = false)
    private List<String> owerIdList;

    //仓库id
    @TableField(exist = false)
    private List<String> warehouseIdList;

    //库存盘点ID
    @TableField(exist = false)
    private List<Long> notCheckIdList;

    @ApiModelProperty(value = "货主ID")
    private Long owerId;

    @ApiModelProperty(value = "货主编号")
    private String owerCode;

    @ApiModelProperty(value = "货主名称")
    private String owerName;

    @ApiModelProperty(value = "库区ID")
    private Long warehouseAreaId;

    @ApiModelProperty(value = "库区编号")
    private String warehouseAreaCode;

    @ApiModelProperty(value = "库区名称")
    private String warehouseAreaName;
}
