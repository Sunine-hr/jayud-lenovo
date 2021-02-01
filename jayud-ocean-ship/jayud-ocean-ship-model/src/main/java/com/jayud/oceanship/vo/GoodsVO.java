package com.jayud.oceanship.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 货品信息表
 * </p>
 *
 * @author
 * @since
 */
@Data
public class GoodsVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "标签（空运是唛头）")
    private String label;

    @ApiModelProperty(value = "货品名称")
    private String name;

    @ApiModelProperty(value = "板数")
    private Integer plateAmount;

    @ApiModelProperty(value = "板数单位")
    private String plateUnit;

    @ApiModelProperty(value = "散货件数")
    private Integer bulkCargoAmount;

    @ApiModelProperty(value = "散货单位")
    private String bulkCargoUnit;

    @ApiModelProperty(value = "尺寸(长宽高)")
    private String size;

    @ApiModelProperty(value = "总重量")
    private Double totalWeight;

    @ApiModelProperty(value = "体积")
    private Double volume;

    @ApiModelProperty(value = "业务类型(0:空运)")
    private Integer businessType;

    @ApiModelProperty(value = "业务主键(根据类型选择对应表的主键)")
    private Long businessId;


}
