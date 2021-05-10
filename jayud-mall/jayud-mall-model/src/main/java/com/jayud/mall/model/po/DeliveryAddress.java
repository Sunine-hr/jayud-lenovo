package com.jayud.mall.model.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
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
 * 提货地址基础数据表
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="DeliveryAddress对象", description="提货、收货地址基础数据表")
public class DeliveryAddress extends Model<DeliveryAddress> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(ordinal = 1)
    private Integer id;

    @ApiModelProperty(value = "客户ID(customer id)", position = 2)
    @JSONField(ordinal = 2)
    private Integer customerId;

    @ApiModelProperty(value = "类型(1提货地址 2收货地址)", position = 3)
    @JSONField(ordinal = 3)
    private Integer types;

    @ApiModelProperty(value = "联系人", position = 4)
    @JSONField(ordinal = 4)
    private String contacts;

    @ApiModelProperty(value = "联系电话", position = 5)
    @JSONField(ordinal = 5)
    private String phone;

    @ApiModelProperty(value = "国家代码", position = 6)
    @JSONField(ordinal = 6)
    private String countryCode;

    @ApiModelProperty(value = "国家名称", position = 7)
    @JSONField(ordinal = 7)
    private String countryName;

    @ApiModelProperty(value = "省/州代码", position = 8)
    @JSONField(ordinal = 8)
    private String stateCode;

    @ApiModelProperty(value = "省/州名称", position = 9)
    @JSONField(ordinal = 9)
    private String stateName;

    @ApiModelProperty(value = "城市代码", position = 10)
    @JSONField(ordinal = 10)
    private String cityCode;

    @ApiModelProperty(value = "城市名称", position = 11)
    @JSONField(ordinal = 11)
    private String cityName;

    @ApiModelProperty(value = "区县代码")
    private String regionCode;

    @ApiModelProperty(value = "区县名称")
    private String regionName;

    @ApiModelProperty(value = "地址1", position = 12)
    @JSONField(ordinal = 12)
    private String address;

    @ApiModelProperty(value = "地址2", position = 13)
    @JSONField(ordinal = 13)
    private String address2;

    @ApiModelProperty(value = "地址3", position = 14)
    @JSONField(ordinal = 14)
    private String address3;

    @ApiModelProperty(value = "邮编", position = 15)
    @JSONField(ordinal = 15)
    private String zipCode;

    @ApiModelProperty(value = "仓库代码(fab_warehouse warehouse_code)", position = 16)
    @JSONField(ordinal = 16)
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称(fab_warehouse warehouse_name)", position = 17)
    @JSONField(ordinal = 17)
    private String warehouseName;

    @ApiModelProperty(value = "启用状态0-禁用，1-启用", position = 18)
    @JSONField(ordinal = 18)
    private Integer status;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
