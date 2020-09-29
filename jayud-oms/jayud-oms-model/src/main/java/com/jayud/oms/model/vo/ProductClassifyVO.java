package com.jayud.oms.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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

    @ApiModelProperty(value = "步骤描述集合")
    private String[] descs;

    @ApiModelProperty(value = "子集合")
    private List<ProductClassifyVO> productClassifyVOS;


}
