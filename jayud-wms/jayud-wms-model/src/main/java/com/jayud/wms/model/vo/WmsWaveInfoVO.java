package com.jayud.wms.model.vo;

import com.jayud.wms.model.po.WmsWaveOrderInfo;
import com.jayud.wms.model.po.WmsWaveToMaterial;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author ciro
 * @date 2021/12/27 9:16
 * @description: 波次单VO
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="波次单VO", description="波次单VO")
public class WmsWaveInfoVO extends WmsWaveOrderInfo {


    @ApiModelProperty(value = "出库单号集合")
    List<String> orderNumberList;

    @ApiModelProperty(value = "物料信息集合")
    List<WmsWaveToMaterial> materialList;

    @ApiModelProperty(value = "波次编号集合")
    List<String> waveOrderNumberList;

    @ApiModelProperty(value = "是否新增")
    Boolean isAdd;

    @ApiModelProperty(value = "是否删除")
    Boolean isDel;
}
