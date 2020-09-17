package com.jayud.oms.model.po;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 子订单变量历史记录表
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OrderTransportHistory对象", description="子订单变量历史记录表")
public class OrderTransportHistory extends Model<OrderTransportHistory> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "主订单号")
    private String mainOrderNo;

    @ApiModelProperty(value = "子订单号")
    private String orderNo;

    @ApiModelProperty(value = "操作描述")
    private String remarks;

    @ApiModelProperty(value = "操作人id")
    private Integer optUid;

    @ApiModelProperty(value = "操作人")
    private String optUname;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
