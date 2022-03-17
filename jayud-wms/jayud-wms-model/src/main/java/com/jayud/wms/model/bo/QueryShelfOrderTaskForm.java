package com.jayud.wms.model.bo;

import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * ShelfOrderTask 实体类
 *
 * @author jyd
 * @since 2021-12-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="上架任务单对象", description="上架任务单")
public class QueryShelfOrderTaskForm extends SysBaseEntity {


    @ApiModelProperty(value = "所属仓库id")
    private Long warehouseId;

    @ApiModelProperty(value = "状态(1:待上架,2:上架中,3:已上架,4:强制上架,5:撤销上架)")
    private Integer status;

    @ApiModelProperty(value = "上架单号(生成上架任务生成的)")
    private String shelfNum;

    @ApiModelProperty(value = "物料编号")
    private String materialCode;

    @ApiModelProperty(value = "收货单号")
    private String orderNum;

    @ApiModelProperty(value = "容器号")
    private String containerNum;




    //公司
    private List<String> orgIds;

    //是否在这个体系内
    private Boolean isCharge;

    //货主id
    private List<String> owerIdList;

    //仓库id
    private List<String> warehouseIdList;

}
