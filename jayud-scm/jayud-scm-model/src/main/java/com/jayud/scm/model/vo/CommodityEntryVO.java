package com.jayud.scm.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 商品申报要素明细表
 * </p>
 *
 * @author LLJ
 * @since 2021-07-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CommodityEntryVO {

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "商品ID")
    private Integer commodityId;

    @ApiModelProperty(value = "要素名称")
    private String elementName;

    @ApiModelProperty(value = "要素值")
    private String elementValue;

    @ApiModelProperty(value = "排序")
    private Integer elementSort;

    @ApiModelProperty(value = "备注")
    private String remark;


}
