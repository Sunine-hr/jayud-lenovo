package com.jayud.mall.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 集货仓表
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ShippingArea对象", description="集货仓表")
public class ShippingArea extends Model<ShippingArea> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "仓库代码")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "国家代码")
    private String stateCode;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "联系电话")
    private String contactNumber;

    @ApiModelProperty(value = "联系手机")
    private String contactPhone;

    @ApiModelProperty(value = "地址1")
    private String addressFirst;

    @ApiModelProperty(value = "地址2")
    private String addressSecond;

    @ApiModelProperty(value = "地址3")
    private String addressThirdly;

    @ApiModelProperty(value = "省id")
    private Integer pid;

    @ApiModelProperty(value = "省/州名")
    private String pname;

    @ApiModelProperty(value = "城市id")
    private Integer cid;

    @ApiModelProperty(value = "城市名")
    private String cname;

    @ApiModelProperty(value = "邮编")
    private String zipCode;

    @ApiModelProperty(value = "进仓单文件模版")
    private String deliveryNote;

    @ApiModelProperty(value = "唛头文件模版URL")
    private String shippingMark;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

    @ApiModelProperty(value = "创建用户id")
    private Integer userId;

    @ApiModelProperty(value = "创建用户名")
    private String userName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
