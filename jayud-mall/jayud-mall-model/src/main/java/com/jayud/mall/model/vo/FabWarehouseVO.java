package com.jayud.mall.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FabWarehouseVO {

    private Integer id;

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

    @ApiModelProperty(value = "状态(0无效 1有效)")
    @TableField(value = "`status`")
    private String status;

    @ApiModelProperty(value = "创建用户id")
    private Integer userId;

    @ApiModelProperty(value = "创建用户名")
    private String userName;

    private LocalDateTime createTime;


}
