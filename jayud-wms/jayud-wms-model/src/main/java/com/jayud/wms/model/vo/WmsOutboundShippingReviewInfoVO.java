package com.jayud.wms.model.vo;

import com.jayud.wms.model.po.WmsOutboundShippingReviewInfo;
import com.jayud.wms.model.po.WmsOutboundShippingReviewInfoToMaterial;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author ciro
 * @date 2022/4/7 14:29
 * @description:wms-发运复核vo
 */
@Data
@ApiModel(value="wms-出库发运复核对象", description="wms-出库发运复核")
public class WmsOutboundShippingReviewInfoVO extends WmsOutboundShippingReviewInfo {

    @ApiModelProperty(value = "发运复核物料信息")
    List<WmsOutboundShippingReviewInfoToMaterial> materialList;

    @ApiModelProperty(value = "作业人员id集合")
    private List<Long> operatorsIds;
}
