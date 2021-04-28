package com.jayud.storage.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 仓库区域货架库位表
 * </p>
 *
 * @author LLJ
 * @since 2021-04-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="WarehouseAreaShelvesLocation对象", description="仓库区域货架库位表")
public class WarehouseAreaShelvesLocation extends Model<WarehouseAreaShelvesLocation> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "仓库区域货架id")
    private Long shelvesId;

    @ApiModelProperty(value = "货架层")
    private Integer shelvesLine;

    @ApiModelProperty(value = "货架列数")
    private Integer shelvesColumn;

    @ApiModelProperty(value = "货架类型")
    private String shelvesType;

    @ApiModelProperty(value = "长")
    private Double length;

    @ApiModelProperty(value = "宽")
    private Double width;

    @ApiModelProperty(value = "高")
    private Double height;

    @ApiModelProperty(value = "最大受重")
    private Double maximumWeight;

    @ApiModelProperty(value = "sku种类上限")
    private Integer categoryUpperLimit;

    @ApiModelProperty(value = "1为有物品，0为没有物品")
    private Integer isGood;

    @ApiModelProperty(value = "0为无效，1为有效")
    private Integer status;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "备注")
    private String remarks;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
