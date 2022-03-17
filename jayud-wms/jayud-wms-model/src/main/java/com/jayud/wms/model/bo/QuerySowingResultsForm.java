package com.jayud.wms.model.bo;

import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * SowingResults 实体类
 *
 * @author jyd
 * @since 2021-12-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="播种结果对象", description="播种结果")
public class QuerySowingResultsForm extends SysBaseEntity {


    @ApiModelProperty(value = "播种位编号")
    private String seedingPositionNum;

    @ApiModelProperty(value = "收货单id")
    private Long orderId;

    @ApiModelProperty(value = "收货单号")
    private String orderNum;

    @ApiModelProperty(value = "收货通知单号")
    private String receiptNoticeNum;


    @ApiModelProperty(value = "物料编号")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "原容器号")
    private String oldContainerNum;


    @ApiModelProperty(value = "新容器号")
    private String newContainerNum;

    @ApiModelProperty(value = "状态(1:未更换,2:已更换,3:确认上架)")
    private Integer status;



    //货主id
    private List<String> owerIdList;

    //仓库id
    private List<String> warehouseIdList;

}
