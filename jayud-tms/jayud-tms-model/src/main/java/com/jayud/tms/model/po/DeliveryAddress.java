package com.jayud.tms.model.po;

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
 * @author chuanmei
 * @since 2020-10-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="DeliveryAddress对象", description="提货地址基础数据表")
public class DeliveryAddress extends Model<DeliveryAddress> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "客户ID")
    private Long customerId;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "国家代码")
    private Integer countryCode;

    @ApiModelProperty(value = "国家名称")
    private String countryName;

    @ApiModelProperty(value = "省/州代码")
    private String stateCode;

    @ApiModelProperty(value = "省/州名称")
    private String stateName;

    @ApiModelProperty(value = "城市代码")
    private String cityCode;

    @ApiModelProperty(value = "城市名称")
    private String cityName;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "邮编")
    private String postCode;

    @ApiModelProperty(value = "仓库ID")
    private String warehouseInfoId;

    @ApiModelProperty(value = "启用状态0-禁用，1-启用")
    private Integer status;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "经纬度")
    private String loAndLa;

    @ApiModelProperty(value = "省主键")
    private Long province;

    @ApiModelProperty(value = "市主键")
    private Long city;

    @ApiModelProperty(value = "区主键")
    private Long area;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
