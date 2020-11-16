package com.jayud.mall.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单对应报关文件
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OrderCustomsFile对象", description="订单对应报关文件")
public class OrderCustomsFile extends Model<OrderCustomsFile> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "订单号(order_info id)")
    private Integer orderId;

    @ApiModelProperty(value = "文件代码")
    private String idCode;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "是否必要(0否 1是)")
    private Integer options;

    @ApiModelProperty(value = "是否审核(0否 1是)")
    private Integer isCheck;

    @ApiModelProperty(value = "模版文件地址")
    private String templateUrl;

    @ApiModelProperty(value = "说明")
    private String describe;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
