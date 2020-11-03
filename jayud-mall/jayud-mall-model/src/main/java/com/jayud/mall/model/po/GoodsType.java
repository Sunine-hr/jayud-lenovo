package com.jayud.mall.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * (报价&货物)类型表
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="GoodsType对象", description="(报价&货物)类型表")
public class GoodsType extends Model<GoodsType> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增加")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "货物类型名")
    private String name;

    @ApiModelProperty(value = "父级id")
    private Integer fid;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "启用状态0-禁用，1-启用")
    private String status;

    @ApiModelProperty(value = "类型1报价类型 2货物类型")
    private String types;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
