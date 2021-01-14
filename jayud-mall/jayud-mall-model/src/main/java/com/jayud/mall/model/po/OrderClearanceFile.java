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

    @ApiModelProperty(value = "自增id", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "订单号(order_info id)", position = 2)
    @JSONField(ordinal = 2)
    private Integer orderId;

    @ApiModelProperty(value = "文件代码(quoted_file id_code)", position = 3)
    @JSONField(ordinal = 3)
    private String idCode;

    @ApiModelProperty(value = "文件名称(template_file file_name)", position = 4)
    @JSONField(ordinal = 4)
    private String fileName;

    @ApiModelProperty(value = "是否必要(0否 1是)", position = 5)
    @TableField(value = "`options`")
    @JSONField(ordinal = 5)
    private Integer options;

    @ApiModelProperty(value = "是否审核(0否 1是)", position = 6)
    @JSONField(ordinal = 6)
    private Integer isCheck;

    @ApiModelProperty(value = "模版文件地址", position = 7)
    @JSONField(ordinal = 7)
    private String templateUrl;

    @ApiModelProperty(value = "说明", position = 8)
    @TableField(value = "`describe`")
    @JSONField(ordinal = 8)
    private String describe;

    @ApiModelProperty(value = "审核状态(0审核不通过  1审核通过)", position = 9)
    @JSONField(ordinal = 9)
    private Integer auditStatus;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
