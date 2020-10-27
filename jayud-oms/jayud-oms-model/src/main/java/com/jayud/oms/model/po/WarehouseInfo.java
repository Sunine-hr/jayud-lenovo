package com.jayud.oms.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * 仓库信息表
 * </p>
 *
 * @author chuanmei
 * @since 2020-10-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="WarehouseInfo对象", description="仓库信息表")
public class WarehouseInfo extends Model<WarehouseInfo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "仓库代码")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "联系电话")
    private String contactNumber;

    @ApiModelProperty(value = "联系手机")
    private String contactPhone;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "省代码")
    private String stateCode;

    @ApiModelProperty(value = "国家代码")
    private String countryCode;

    @ApiModelProperty(value = "城市名")
    private String cityCode;

    @ApiModelProperty(value = "邮编")
    private String zipCode;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

    @ApiModelProperty(value = "创建用户名")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
