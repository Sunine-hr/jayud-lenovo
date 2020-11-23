package com.jayud.mall.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "(报价&货物)类型表")
public class GoodsTypeForm {

    @ApiModelProperty(value = "自增加")
    private Long id;

    @ApiModelProperty(value = "货物类型名")
    private String name;

    @ApiModelProperty(value = "父级id")
    private Integer fid;

    @ApiModelProperty(value = "启用状态0-禁用，1-启用")
    private String status;

    @ApiModelProperty(value = "类型1报价类型 2货物类型")
    private String types;

}
