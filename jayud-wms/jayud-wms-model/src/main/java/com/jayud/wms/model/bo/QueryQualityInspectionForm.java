package com.jayud.wms.model.bo;

import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * QualityInspection 实体类
 *
 * @author jyd
 * @since 2021-12-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "质检检测对象", description = "质检检测")
public class QueryQualityInspectionForm extends SysBaseEntity {


    @ApiModelProperty(value = "所属仓库id")
    private Long warehouseId;

    @ApiModelProperty(value = "所属仓库名称")
    private String warehouse;

    @ApiModelProperty(value = "质检单号")
    private String qcNo;

    @ApiModelProperty(value = "收货单号")
    private String receiptNum;

    @ApiModelProperty(value = "质检状态(1:未质检,2:已质检)")
    private Integer status;



    @ApiModelProperty(value = "质检时间")
    private List<String> theQualityOfTime;



    private List<String> orgIds;

    //是否在这个体系内
    private Boolean isCharge;

    //货主id
    private List<String> owerIdList;

    //仓库id
    private List<String> warehouseIdList;

}
