package com.jayud.wms.model.bo;

import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class QueryWarehouseShelfForm extends SysBaseEntity {

    @ApiModelProperty(value = "查询类型(input入库, output出库)")
    private String queryType;

    @ApiModelProperty(value = "货架编号")
    private String shelfcode;



}
