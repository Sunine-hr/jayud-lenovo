package com.jayud.common.dto;

import com.jayud.common.entity.SysBaseEntity;
import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class QueryClientQualityInspectionForm extends SysBaseEntity {

    @ApiModelProperty(value = "仓库id")
    private Long warehouseId;

    @ApiModelProperty(value = "收货单id")
    private Long orderId;

    @ApiModelProperty(value = "收货单号")
    private String orderNum;

    @ApiModelProperty(value = "质检表id")
    private Long qualityInspectionId;

    @ApiModelProperty(value = "物料编号")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "质检单号")
    private String qcNo;

    @ApiModelProperty(value = "检验数量")
    private Integer inspectionQuantity;

    @ApiModelProperty(value = "合格数量")
    private Integer qualifiedQuantity;

    @ApiModelProperty(value = "不合格数量")
    private Integer unqualifiedQuantity;

    @ApiModelProperty(value = "不合格原因（字典值）")
    private String causeNonconformity;



    @ApiModelProperty(value = "所属仓库名称")
    private String warehouse;


    @ApiModelProperty(value = "收货单号")
    private String receiptNum;

    @ApiModelProperty(value = "质检状态(1:未质检,2:已质检)")
    private Integer status;

    @ApiModelProperty(value = "文件字段预留one")
    private List<FileView> fileOne;

    @ApiModelProperty(value = "文件字段预留")
    private List<String> fileList;

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
