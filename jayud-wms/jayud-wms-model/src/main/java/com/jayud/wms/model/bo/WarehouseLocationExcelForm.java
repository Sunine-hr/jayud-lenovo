package com.jayud.wms.model.bo;

import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * WarehouseLocationForm 实体类
 *
 * @author jyd
 * @since 2021-12-14
 */
@Data
public class WarehouseLocationExcelForm extends SysBaseEntity {


    @ApiModelProperty(value = "库位编号")
    private String code;

    @ApiModelProperty(value = "库位类型")
    private String typeDesc;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "库区名称")
    private String warehouseAreaName;

    @ApiModelProperty(value = "排数")
    private Integer row;

    @ApiModelProperty(value = "列数")
    private Integer columnNum;

    @ApiModelProperty(value = "层数")
    private Integer layers;

    @ApiModelProperty(value = "长*宽*高")
    private String lengths;

    @ApiModelProperty(value = "是否允许混放")
    private String isMixing;

    @ApiModelProperty(value = "是否入库冻结")
    private String isInFrozen;

    @ApiModelProperty(value = "是否出库冻结")
    private String isOutFrozen;

    @ApiModelProperty(value = "备注信息")
    private String remark;

    public String checkParam(){
        if(this.code == null){
            return "库位编号不为空";
        }
        if(this.typeDesc == null){
            return "库位类型不为空";
        }
        if(this.warehouseName == null){
            return "仓库名称不为空";
        }
        if(this.warehouseAreaName == null){
            return "库区名称不为空";
        }
        if(this.row == null){
            return "排数不为空";
        }
        if(this.columnNum == null){
            return "列数不为空";
        }
        if(this.layers == null){
            return "层数不为空";
        }
        if(this.lengths == null){
            return "长*宽*高不为空";
        }
        return "成功";
    }

}
