package com.jayud.oms.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.jayud.common.utils.Utilities;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "CustomerInfo对象", description = "客户信息表")
public class CustomerInfo extends Model<CustomerInfo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "客户名")
    private String name;

    @ApiModelProperty(value = "客户代码")
    private String idCode;

    @ApiModelProperty(value = "客户类型(1同行 2电商 3货代)")
    private Integer types;

    @ApiModelProperty(value = "联系人")
    private String contact;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "纳税号")
    private String tfn;

    @ApiModelProperty(value = "是否有合同(0否 1是)")
    private String ifContract;

    @ApiModelProperty(value = "结算类型(1票结 2月结 3周结)")
    private Integer settlementType;

    @ApiModelProperty(value = "账期")
    private String accountPeriod;

    @ApiModelProperty(value = "税票种类")
    private String taxType;

    @ApiModelProperty(value = "税率")
    private String taxRate;

    @ApiModelProperty(value = "等级")
    private Integer estate;

    @ApiModelProperty(value = "对应部门code")
    private String departmentId;

    @ApiModelProperty(value = "对应客服ID")
    private Long kuId;

    @ApiModelProperty(value = "对应业务员ID")
    private Long ywId;

    @ApiModelProperty(value = "审核状态")
    private String auditStatus;

    @ApiModelProperty(value = "审核意见")
    private String auditComment;

    @ApiModelProperty(value = "创建人")
    private String createdUser;

    @ApiModelProperty(value = "更新人")
    private String updatedUser;

    @ApiModelProperty(value = "修改时间")
    private Timestamp updatedTime;

    @ApiModelProperty(value = "创建时间")
    private Timestamp createdTime;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

    @ApiModelProperty(value = "国家征信")
    private Integer nationalCredit;

    @ApiModelProperty(value = "海关征信")
    private Integer customsCredit;

    @ApiModelProperty(value = "海关信用等级(0:一般认证企业,1:一般信用企业,2:高级信用企业,3:失信企业)")
    private Integer customsCreditRating;

    @ApiModelProperty(value = "是否高级认证")
    private Boolean isAdvancedCertification;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    public static void main(String[] args) {
        System.out.println(Utilities.printFieldsInfo(CustomerInfo.class));
    }
}
