package com.jayud.wms.model.bo;

import com.jayud.common.entity.SysBaseEntity;
import com.jayud.common.exception.ServiceException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * IncomingSeeding 实体类
 *
 * @author jyd
 * @since 2021-12-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "入库播种对象", description = "入库播种")
public class IncomingSeedingForm extends SysBaseEntity {


    @ApiModelProperty(value = "收货单id")
    private Long orderId;

    @ApiModelProperty(value = "收货单号")
    private String orderNum;

    @ApiModelProperty(value = "收货通知单号")
    private String receiptNoticeNum;

    @ApiModelProperty(value = "物料id")
    private Long materialId;

    @ApiModelProperty(value = "物料编号")
    @NotEmpty(message = "物料编号不能为空")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    @NotEmpty(message = "物料编号不能为空")
    private String materialName;

    @ApiModelProperty(value = "容器号")
    @NotEmpty(message = "容器号不能为空")
    private String containerNum;

    @ApiModelProperty(value = "数量")
    @NotNull(message = "总数量不能为空")
    private Double num;

    @ApiModelProperty(value = "已分配数量")
    @NotNull(message = "已分配数量不能为空")
    private Double allocatedQuantity;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注信息")
    private String remark;


    @ApiModelProperty(value = "是否删除")
    //@TableLogic //@TableLogic注解表示逻辑删除,设置修改人手动更新，这里注释
    private Boolean isDeleted;

    @ApiModelProperty(value = "批次号")
    private String batchNum;

    @ApiModelProperty(value = "生产日期")
    private LocalDate productionDate;

    @ApiModelProperty(value = "自定义字段1(1:合格,2:不合格)(是否合格,可能后续上架策略会使用)")
    private String columnOne;

    @ApiModelProperty(value = "自定义字段2")
    private String columnTwo;

    @ApiModelProperty(value = "自定义字段3")
    private String columnThree;


    @ApiModelProperty(value = "新容器编号")
    @NotEmpty(message = "新容器编号不能为空")
    private String newContainerNum;

    @ApiModelProperty(value = "新容器名称")
    @NotEmpty(message = "新容器名称不能为空")
    private String newContainerName;

    @ApiModelProperty(value = "更换数量")
    @NotNull(message = "更换数量不能为空")
    private Double newNum;

    @ApiModelProperty(value = "播种位编号")
    @NotEmpty(message = "播种位编号不能为空")
    private String seedingPositionNum;


    public void checkParam() {
        if (allocatedQuantity + newNum > num) {
            throw new ServiceException("更换数量超过总数量");
        }
        if (newNum==0.0){
            throw new ServiceException("数量不能为0");
        }
    }
}
