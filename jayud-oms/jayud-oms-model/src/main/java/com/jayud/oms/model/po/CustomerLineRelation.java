package com.jayud.oms.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 客户线路客户列表
 * </p>
 *
 * @author CYC
 * @since 2021-10-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="CustomerLineRelation对象", description="客户线路客户列表")
public class CustomerLineRelation extends Model<CustomerLineRelation> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "客户线路ID")
    private Long customerLineId;

    @ApiModelProperty(value = "客户ID")
    private Long customerId;

    @TableField(exist = false)
    @ApiModelProperty(value = "客户编号")
    private String customerCode;

    @TableField(exist = false)
    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
