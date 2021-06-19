package com.jayud.storage.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 商品对应库位表（记录入库的商品所在库位以及数量）
 * </p>
 *
 * @author LLJ
 * @since 2021-04-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="GoodsLocationRecord对象", description="商品对应库位表（记录入库的商品所在库位以及数量）")
public class GoodsLocationRecord extends Model<GoodsLocationRecord> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "库位code")
    private String kuCode;

    @ApiModelProperty(value = "入库商品id")
    @TableField("inGood_id")
    private Long inGoodId;

    @ApiModelProperty(value = "入库商品数量")
    private Integer number;

    @ApiModelProperty(value = "未出库商品数量")
    private Integer unDeliveredQuantity;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "存储类型 (1为入库，2为出库)")
    private Integer type;

    @ApiModelProperty(value = "是否已拣货 (1为已拣货，2为未拣货)")
    private Integer isPickedGoods;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
