package com.jayud.mall.model.po;

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
 * 订单对应清关文件
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OrderClearanceFile对象", description="订单对应清关文件")
public class OrderClearanceFile extends Model<OrderClearanceFile> {

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
    @TableField(value = "`options`")
    private Integer options;

    @ApiModelProperty(value = "是否审核(0否 1是)")
    private Integer isCheck;

    @ApiModelProperty(value = "模版文件地址")
    private String templateUrl;

    @ApiModelProperty(value = "说明")
    @TableField(value = "`describe`")
    private String describe;

    @ApiModelProperty(value = "审核状态(0审核不通过  1审核通过)")
    private Integer auditStatus;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
