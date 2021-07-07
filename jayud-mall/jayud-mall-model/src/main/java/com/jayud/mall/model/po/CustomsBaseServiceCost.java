package com.jayud.mall.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 海关基础资料-服务费用表
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "CustomsBaseServiceCost对象", description = "海关基础资料-服务费用表")
public class CustomsBaseServiceCost extends Model<CustomsBaseServiceCost> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "类型(1报关 2清关)")
    private Integer type;

    @ApiModelProperty(value = "海关基础资料id(customs_data id customs_clearance id)")
    private Long customsId;

    @ApiModelProperty(value = "服务id(service_group id)")
    private Long serviceId;

    @ApiModelProperty(value = "单价")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "币种(currency_info id)")
    private Integer cid;

    @ApiModelProperty(value = "单位(1公斤 2方 3票 4柜)")
    private Integer unit;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "服务名称(service_group code_name)")
    private String serviceName;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
