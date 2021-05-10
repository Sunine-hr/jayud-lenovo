package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(value = "可达仓库(fab_warehouse)")
public class FabWarehouseForm {

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

    @ApiModelProperty(value = "国家代码", position = 4)
    @JSONField(ordinal = 4)
    private String stateCode;

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

    @ApiModelProperty(value = "省id", position = 12)
    @JSONField(ordinal = 12)
    private Integer pid;

    @ApiModelProperty(value = "省/州名", position = 13)
    @JSONField(ordinal = 13)
    private String pname;

    @ApiModelProperty(value = "城市id", position = 14)
    @JSONField(ordinal = 14)
    private Integer cid;

    @ApiModelProperty(value = "城市名", position = 15)
    @JSONField(ordinal = 15)
    private String cname;

    @ApiModelProperty(value = "邮编", position = 16)
    @JSONField(ordinal = 16)
    private String zipCode;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 17)
    @JSONField(ordinal = 17)
    @TableField(value = "`status`")
    private String status;

    @ApiModelProperty(value = "创建用户id", position = 18)
    @JSONField(ordinal = 18)
    private Integer userId;

    @ApiModelProperty(value = "创建用户名", position = 19)
    @JSONField(ordinal = 19)
    private String userName;

    @ApiModelProperty(value = "区域分组", position = 20)
    @JSONField(ordinal = 20)
    private String areaGroup;

    private LocalDateTime createTime;

}
