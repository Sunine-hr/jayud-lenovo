package com.jayud.oms.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 云报关-金蝶财务费用项名称，类型，税率对照表
 * </p>
 *
 * @author Daniel
 */
@Data
public class CustomsFinanceFeeRelation{

    private Integer id;

    @ApiModelProperty(value = "云报关费用代码")
    private String yunbaoguanCode;

    @ApiModelProperty(value = "云报关费用名称")
    private String yunbaoguanName;

    @ApiModelProperty(value = "金蝶费用代码")
    private String kingdeeCode;

    @ApiModelProperty(value = "金蝶费用名称")
    private String kingdeeName;

    @ApiModelProperty(value = "费用类别代码")
    private String categoryCode;

    @ApiModelProperty(value = "费用类别")
    private String category;

    @ApiModelProperty(value = "费用类型代码")
    private String typeCode;

    @ApiModelProperty(value = "费用类型")
    private String type;

    @ApiModelProperty(value = "税率")
    private String taxRate;

    @ApiModelProperty(value = "是否作废(0-否，1-是)")
    private Integer deprecated;
}
