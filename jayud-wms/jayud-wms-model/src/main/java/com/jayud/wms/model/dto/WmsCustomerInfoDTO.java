package com.jayud.wms.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class WmsCustomerInfoDTO {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "客户编码")
    private String customerCode;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "客户类型id")
    private Long customerTypeId;


    @ApiModelProperty(value = "联系人")
    private String userName;

    @ApiModelProperty(value = "手机号码")
    private String phoneNumber;


    @ApiModelProperty(value = "电子邮箱")
    private String email;

    @ApiModelProperty(value = "联系地址")
    private String address;

    @ApiModelProperty(value = "是否可用，1是，0否")
    private Boolean status;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 删除状态：0-未删除，1-已删除
     */
    private Boolean isDeleted;

    @ApiModelProperty(value = "创建人名称")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "最后修改人名称")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


    //WmsCustomerInfo 开发配置
    @ApiModelProperty(value = "开发配置appId")
    private String appId;

    @ApiModelProperty(value = "开发配置appSecret")
    private String appSecret;

    @ApiModelProperty(value = "rsa加密公钥")
    private String publicKey;

    @ApiModelProperty(value = "rsa加密私钥")
    private String privateKey;


}
