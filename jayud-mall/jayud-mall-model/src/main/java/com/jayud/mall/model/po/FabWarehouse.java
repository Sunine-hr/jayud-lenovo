package com.jayud.mall.model.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 应收/FBA仓库
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="FabWarehouse对象", description="应收FBA仓库")
public class FabWarehouse extends Model<FabWarehouse> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(ordinal = 1)
    private Integer id;

    @ApiModelProperty(value = "仓库代码", position = 2)
    @JSONField(ordinal = 2)
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称", position = 3)
    @JSONField(ordinal = 3)
    private String warehouseName;

    @ApiModelProperty(value = "联系人", position = 5)
    @JSONField(ordinal = 5)
    private String contacts;

    @ApiModelProperty(value = "公司名称", position = 6)
    @JSONField(ordinal = 6)
    private String companyName;

    @ApiModelProperty(value = "联系电话", position = 7)
    @JSONField(ordinal = 7)
    private String contactNumber;

    @ApiModelProperty(value = "联系手机", position = 8)
    @JSONField(ordinal = 8)
    private String contactPhone;

    @ApiModelProperty(value = "地址1", position = 9)
    @JSONField(ordinal = 9)
    private String addressFirst;

    @ApiModelProperty(value = "地址2", position = 10)
    @JSONField(ordinal = 10)
    private String addressSecond;

    @ApiModelProperty(value = "地址3", position = 11)
    @JSONField(ordinal = 11)
    private String addressThirdly;

    @ApiModelProperty(value = "邮编", position = 16)
    @JSONField(ordinal = 16)
    private String zipCode;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 17)
    @TableField(value = "`status`")
    @JSONField(ordinal = 17)
    private String status;

    @ApiModelProperty(value = "创建用户id(system_user id)", position = 18)
    @JSONField(ordinal = 18)
    private Integer userId;

    @ApiModelProperty(value = "创建用户名(system_user name)", position = 19)
    @JSONField(ordinal = 19)
    private String userName;

    @ApiModelProperty(value = "创建时间", position = 20)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 20, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "区域分组", position = 21)
    @JSONField(ordinal = 21)
    private String areaGroup;

    @ApiModelProperty(value = "国家代码")
    private String countryCode;

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

    @ApiModelProperty(value = "区县代码")
    private String regionCode;

    @ApiModelProperty(value = "区县名称")
    private String regionName;

    @ApiModelProperty(value = "审核状态(0待审核 1审核通过 2审核不通过)")
    private Integer auditStatus;

    @ApiModelProperty(value = "审核用户id(system_user id)")
    private Integer auditUserId;

    @ApiModelProperty(value = "审核用户名(system_user name)")
    private String auditUserName;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
