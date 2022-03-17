package com.jayud.wms.model.vo;

import com.jayud.wms.model.po.WmsMaterialToLoactionRelation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ciro
 * @date 2021/12/16 11:32
 * @description: 物料和库位关系vo
 */
@Data
public class WmsMaterialToLoactionRelationVO extends WmsMaterialToLoactionRelation {

    @ApiModelProperty(value = "库位编码")
    private String code;

    @ApiModelProperty(value = "是否选中")
    private Boolean isSelect;



}
