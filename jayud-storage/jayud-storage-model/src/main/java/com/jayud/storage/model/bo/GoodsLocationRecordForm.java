package com.jayud.storage.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * 商品对应库位表（记录入库的商品所在库位以及数量）
 * </p>
 *
 * @author LLJ
 * @since 2021-04-19
 */
@Data
public class GoodsLocationRecordForm extends Model<GoodsLocationRecordForm> {

    @ApiModelProperty(value = "库位id")
    private String kuCode;

    @ApiModelProperty(value = "入库商品id")
    private Long inGoodId;

    @ApiModelProperty(value = "入库商品数量")
    private Integer number;

    @ApiModelProperty(value = "未出库商品数量")
    private Integer unDeliveredQuantity;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


}
