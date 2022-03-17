package com.jayud.wms.model.bo;

import com.jayud.common.entity.SysBaseEntity;
import com.jayud.common.exception.ServiceException;
import com.jayud.common.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * QualityInspectionMaterial 实体类
 *
 * @author jyd
 * @since 2021-12-22
 */
@Data
//@EqualsAndHashCode(callSuper = false)
//@Accessors(chain = true)
@ApiModel(value = "质检物料信息对象", description = "质检物料信息")
public class QueryQualityInspectionMaterialForm extends SysBaseEntity {


    @ApiModelProperty(value = "收货单id")
    private Long orderId;

    @ApiModelProperty(value = "收货单号")
    private String orderNum;


    @ApiModelProperty(value = "质检单号")
    private String qcNo;

    @ApiModelProperty(value = "物料编号")
    private String materialCode;

    @ApiModelProperty(value = "实收数量")
    private Double actualNum;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "检验数量")
    private Integer inspectionQuantity;

    @ApiModelProperty(value = "合格数量")
    private Integer qualifiedQuantity;

    @ApiModelProperty(value = "不合格数量")
    private Integer unqualifiedQuantity;

    @ApiModelProperty(value = "不合格原因（字典值）")
    private String causeNonconformity;

    @ApiModelProperty(value = "上传文件")
    private String description;


    public void checkParam() {

        if (this.actualNum < this.inspectionQuantity) {
            throw new ServiceException("校验数量不能超过收货数量");
        }
        if (this.inspectionQuantity != (this.qualifiedQuantity + this.unqualifiedQuantity)) {
            throw new ServiceException("合格数量加不合格数量不等于检验数量");
        }
        if (this.unqualifiedQuantity > 0 && StringUtils.isEmpty(causeNonconformity)) {
            throw new ServiceException("请填写不合格原因");
        }
    }
}
