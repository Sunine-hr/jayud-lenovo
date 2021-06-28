package com.jayud.oms.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 产品分类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Data
public class ProductClassifyVO extends Model<ProductClassifyVO> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增ID")
    private Long id;

    @ApiModelProperty(value = "产品分类编码")
    private String idCode;

    @ApiModelProperty(value = "产品名")
    private String name;

    @ApiModelProperty(value = "父级id")
    private Long fId;

    @ApiModelProperty(value = "是否可选物流服务 0-不可 1-可以")
    private String isOptional;

    @ApiModelProperty(value = "清晰的图片")
    private String obviousPic;

    @ApiModelProperty(value = "模糊的图片")
    private String vaguePic;

    @ApiModelProperty(value = "步骤描述集合")
    private String[] descs;

    @ApiModelProperty(value = "是否已选模块(追加模块功能使用)")
    private Boolean moduleSelected;

    @ApiModelProperty(value = "子集合")
    private List<ProductClassifyVO> productClassifyVOS = new ArrayList<>();


}
