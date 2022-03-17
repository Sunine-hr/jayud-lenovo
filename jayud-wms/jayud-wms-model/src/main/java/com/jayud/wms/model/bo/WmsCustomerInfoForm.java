package com.jayud.wms.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;


/**
 * <p>
 * 客户信息
 * </p>
 */
@Data
public class WmsCustomerInfoForm implements Serializable {

    private static final long serialVersionUID = 3956287435612937606L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "客户编码")
    private String customerCode;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "客户类型id")
    private Long customerTypeId;

    @ApiModelProperty(value = "接受客户类型id")
    private String customerTypeIds;

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

    @ApiModelProperty(value = "排序")
    private Long order;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;

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


    @ApiModelProperty(value = "客户类型详情")
    private String customerTypeDetails;

    //导入使用
    @ApiModelProperty(value = "是否可用，1是，0否")
    private String statusString;

    public String checkParam(){
        if(this.customerCode == null){
            return "客户编号不为空";
        }
        if(this.customerName == null){
            return "客户名称不为空";
        }
        if(this.customerTypeDetails == null){
            return "客户类型不为空";
        }
        if(this.userName == null){
            return "联系人不为空";
        }
        if(this.phoneNumber == null){
            return "手机号不为空";
        }
        if(this.email == null){
            return "电子邮箱不为空";
        }
        if(this.address == null){
            return "联系地址不为空";
        }
        return "成功";
    }



}
