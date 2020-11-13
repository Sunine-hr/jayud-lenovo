package com.jayud.mall.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "OceanWaybillVO", description = "运单表对象")
public class OceanWaybillVO {

    @ApiModelProperty(value = "主键Id")
    private Long id;

    @ApiModelProperty(value = "运单号")
    private String waybillNo;

    @ApiModelProperty(value = "描述")
    @TableField(value = "`describe`")
    private String describe;

    @ApiModelProperty(value = "送货信息")
    private String shippingInformation;

    @ApiModelProperty(value = "货柜信息id(ocean_counter id)")
    private Integer oceanCounterId;

    //1个运单对应N个箱号
    @ApiModelProperty(value = "1个运单对应N个箱号")
    private List<OceanWaybillCaseRelationVO> oceanWaybillCaseRelationVOList;


}
