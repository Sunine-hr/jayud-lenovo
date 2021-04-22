package com.jayud.mall.model.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * (配载单)配置对应的报价与提单
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OceanConfDetail对象", description="(配载单)配置对应的报价与提单")
public class OceanConfDetail extends Model<OceanConfDetail> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "配载id(order_conf id)")
    @JSONField(ordinal = 2)
    private Long orderId;

    @ApiModelProperty(value = "报价/提单id(offer_info id  ocean_bill id)")
    @JSONField(ordinal = 3)
    private Integer idCode;

    @ApiModelProperty(value = "分类区分当前是报价或提单(1报价 2提单)")
    @JSONField(ordinal = 4)
    private Integer types;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    @TableField(value = "`status`")
    @JSONField(ordinal = 5)
    private String status;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
