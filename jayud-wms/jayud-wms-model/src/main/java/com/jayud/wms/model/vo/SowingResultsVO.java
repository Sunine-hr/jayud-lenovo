package com.jayud.wms.model.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.wms.model.enums.SowingResultsStatusEnum;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * SowingResults 实体类
 *
 * @author jyd
 * @since 2021-12-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "播种结果对象", description = "播种结果")
public class SowingResultsVO extends SysBaseEntity {


    @ApiModelProperty(value = "播种位编号")
    private String seedingPositionNum;

    @ApiModelProperty(value = "收货单id")
    private Long orderId;

    @ApiModelProperty(value = "收货单号")
    private String orderNum;

    @ApiModelProperty(value = "收货通知单号")
    private String receiptNoticeNum;

    @ApiModelProperty(value = "物料id")
    private Long materialId;

    @ApiModelProperty(value = "物料编号")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "原容器号")
    private String oldContainerNum;

    @ApiModelProperty(value = "数量")
    private Integer oldNum;

    @ApiModelProperty(value = "播种数量")
    private Double sowingQuantity;

    @ApiModelProperty(value = "播种后数量")
    private Double quantityAfterSowing;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "新容器号")
    private String newContainerNum;

    @ApiModelProperty(value = "新的播种后数量")
    private Double newQuantityAfterSowing;

    @ApiModelProperty(value = "状态(1:未更换,2:已更换,3:确认上架,4:完成上架)")
    private Integer status;

    @ApiModelProperty(value = "状态详情")
    private String statusDetails;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注信息")
    private String remark;


    @ApiModelProperty(value = "收货单号")
    private String receiptNum;

    @ApiModelProperty(value = "是否删除")
    //@TableLogic //@TableLogic注解表示逻辑删除,设置修改人手动更新，这里注释
    private Boolean isDeleted;


    @ApiModelProperty(value = "'批次号'")
    private String batchNum;

    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "生产日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date productionDate;

    @ApiModelProperty(value = "'备用字段1'")
    private String columnOne;

    @ApiModelProperty(value = "'备用字段2'")
    private String columnTwo;

    @ApiModelProperty(value = "'备用字段3'")
    private String columnThree;



    public void setStatus(Integer status) {
        this.status = status;
        this.statusDetails = SowingResultsStatusEnum.getDesc(status);
    }

}
