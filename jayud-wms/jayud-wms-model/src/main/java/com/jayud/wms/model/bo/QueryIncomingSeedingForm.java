package com.jayud.wms.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

/**
 * IncomingSeeding 实体类
 *
 * @author jyd
 * @since 2021-12-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="入库播种对象", description="入库播种")
public class QueryIncomingSeedingForm {


    @ApiModelProperty(value = "物料编号")
    private String materialCode;

    @NotEmpty(message = "容器号不能为空")
    @ApiModelProperty(value = "容器号")
    private String containerNum;



}
