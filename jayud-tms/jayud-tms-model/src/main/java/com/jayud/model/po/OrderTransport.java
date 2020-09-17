package com.jayud.model.po;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 中港运输订单
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OrderTransport对象", description="中港运输订单")
public class OrderTransport extends Model<OrderTransport> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "主订单(order_no)")
    private String mainOrderNo;

    @ApiModelProperty(value = "订单编号(生成规则product_classify code+随时数)")
    private String orderNo;

    @ApiModelProperty(value = "口岸code(port_info code)")
    private String portCode;

    @ApiModelProperty(value = "口岸名称(port_info name)")
    private String portName;

    @ApiModelProperty(value = "货物流向(1进口 2出口)")
    private Integer goodsType;

    @ApiModelProperty(value = "车型(1吨车 2柜车)")
    private Integer vehicleType;

    @ApiModelProperty(value = "车型(1-3T 2-5t 3-8T 4-10T)")
    private Integer vehicleSize;

    @ApiModelProperty(value = "柜号")
    private String cntrNo;

    @ApiModelProperty(value = "柜号图片")
    private String cntrPic;

    @ApiModelProperty(value = "仓库代码(warehouse_info)")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称(warehouse_info)")
    private String warehouseName;

    @ApiModelProperty(value = "六联单号")
    private String encode;

    @ApiModelProperty(value = "状态(0-未接单 1-已接单 2-运输中 3-已完成)")
    private Integer status;

    @ApiModelProperty(value = "接单人id")
    private Integer userId;

    @ApiModelProperty(value = "接单人姓名")
    private String userName;

    @ApiModelProperty(value = "接单时间")
    private LocalDateTime optTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建用户")
    private String createUser;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
