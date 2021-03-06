package com.jayud.wms.model.bo;

import com.jayud.wms.model.vo.WmsOutboundOrderInfoToMaterialVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ConfirmInformationForm {

    @ApiModelProperty(value = "查询类型(input入库, output出库)")
    private String queryType;

    @ApiModelProperty(value = "货架编号")
    private String shelfcode;

    @ApiModelProperty(value = "物料编号")
    private String materialCode;

    @ApiModelProperty(value = "库位编号")
    private String warehouseLocationCode;

    @ApiModelProperty(value = "出库单物料")
    private List<WmsOutboundOrderInfoToMaterialVO> materialVoList;
}
