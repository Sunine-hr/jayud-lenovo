package com.jayud.wms.model.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jayud.common.entity.SysBaseEntity;
import com.jayud.common.exception.ServiceException;
import com.jayud.common.utils.StringUtils;
import com.jayud.wms.model.enums.MaterialStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Material 实体类
 *
 * @author jyd
 * @since 2021-12-21
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "货单物料信息对象", description = "货单物料信息")
public class MaterialForm extends SysBaseEntity {


    @ApiModelProperty(value = "收货单id")
    private Long orderId;

    @ApiModelProperty(value = "收货单号")
    private String orderNum;

    @ApiModelProperty(value = "物料编号")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "物料类型id")
    private Long materialTypeId;

    @ApiModelProperty(value = "物料类型")
    private String materialType;

    @ApiModelProperty(value = "预期数量")
    private Double num;

    @ApiModelProperty(value = "实收数量")
    private Double actualNum;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "重量")
    private BigDecimal weight;

    @ApiModelProperty(value = "体积")
    private BigDecimal volume;

    @ApiModelProperty(value = "物料规格")
    private String specification;

    @ApiModelProperty(value = "容器号")
    private String containerNum;

    @ApiModelProperty(value = "收货状态(1:待收货,2:待上架,3:已上架,4:撤销)")
    private Integer status = MaterialStatusEnum.ONE.getCode();

    @ApiModelProperty(value = "外部订单号")
    private String externalOrderNum;

    @ApiModelProperty(value = "外部单行号")
    private String externalLineNum;

    @ApiModelProperty(value = "批次号")
    private String batchNum;

    @ApiModelProperty(value = "生产日期")
    private LocalDate productionDate;

    @ApiModelProperty(value = "自定义字段1")
    private String columnOne;

    @ApiModelProperty(value = "自定义字段2")
    private String columnTwo;

    @ApiModelProperty(value = "自定义字段3")
    private String columnThree;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注信息")
    private String remark;


    @ApiModelProperty(value = "是否删除")
    //@TableLogic //@TableLogic注解表示逻辑删除,设置修改人手动更新，这里注释
    private Boolean isDeleted;


    @ApiModelProperty(value = "是否直接上架")
    private Boolean isPutShelf;

    @ApiModelProperty(value = "计算出实际数量")
    @JsonIgnore
    private BigDecimal tmpNum;

    @ApiModelProperty(value = "实际收货总合")
    @JsonIgnore
    private BigDecimal tmpActualNum;

    @ApiModelProperty(value = "数据是否可以全部填写")
    private Boolean isNewTitle = true;


    @ApiModelProperty(value = "上传文件")
    private String description;

    @ApiModelProperty(value = "库位ID")
    private Long warehouseLocationId;

    @ApiModelProperty(value = "库位编号")
    private String warehouseLocationCode;

    @ApiModelProperty(value = "入仓号")
    private String inWarehouseNumber;

    public void checkParam() {
        if (StringUtils.isEmpty(this.containerNum)) {
            throw new ServiceException("请填写容器号");
        }
    }

    public void calculationStatus() {
        if (this.status != null && this.status.equals(MaterialStatusEnum.FOUR.getCode())){ return;}
        if (this.tmpActualNum.compareTo(this.tmpNum) == 0) {
            this.status = MaterialStatusEnum.THREE.getCode();
        }
        if (this.tmpActualNum.compareTo(this.tmpNum) < 0) {
            this.status = MaterialStatusEnum.TWO.getCode();
        }
        if (this.tmpActualNum.compareTo(new BigDecimal(0)) == 0) {
            this.status = MaterialStatusEnum.ONE.getCode();
        }
    }

//    public MaterialForm setStatus(Integer status) {
//        if (status == null) {
//            this.status = MaterialStatusEnum.ONE.getCode();
//        } else {
//            this.status = status;
//        }
//        return this;
//    }

}
