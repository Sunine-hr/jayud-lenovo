package com.jayud.oms.model.bo;

import com.jayud.common.exception.JayudBizException;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

/**
 * 新增客户信息界面
 */
@Data
public class AddCustomerInfoForm {

    @ApiModelProperty(value = "主键ID,修改时必传")
    private Long id;

    @ApiModelProperty(value = "客户名称",required = true)
    @NotEmpty(message = "name is required")
    private String name;

    @ApiModelProperty(value = "客户代码")
    private String idCode;

    @ApiModelProperty(value = "客户类型(1同行 2电商 3货代)",required = true)
    private Integer types;

    @ApiModelProperty(value = "联系人",required = true)
    @NotEmpty(message = "contact is required")
    private String contact;

    @ApiModelProperty(value = "联系电话",required = true)
    @NotEmpty(message = "phone is required")
    private String phone;

    @ApiModelProperty(value = "地址",required = true)
    @NotEmpty(message = "address is required")
    private String address;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "法人主体ID集合",required = true)
    @NotNull(message = "legalEntityIds is required")
    private List<Long> legalEntityIds = new ArrayList<>();

    @ApiModelProperty(value = "纳税号",required = true)
    @NotEmpty(message = "tfn is required")
    private String tfn;

    @ApiModelProperty(value = "是否有合同(0否 1是)",required = true)
    @Pattern(regexp = "0|1",message = "ifContract requires '0' or '1' only")
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

    @ApiModelProperty(value = "接单部门ID",required = true)
    @NotEmpty(message = "departmentId is required")
    private String departmentId;

    @ApiModelProperty(value = "接单部门")
    private String departmentName;

    @ApiModelProperty(value = "接单客服ID")
    private Long kuId;

    @ApiModelProperty(value = "接单客服")
    private String kuName;

    @ApiModelProperty(value = "业务员ID",required = true)
    @NotNull(message = "ywId is required")
    private Long ywId;

    @ApiModelProperty(value = "业务员")
    private String ywName;

    @ApiModelProperty(value = "当前登录用户,前台传",required = true)
    private String loginUserName;


    @ApiModelProperty(value = "依赖结算单位id集合")
    private List<Long> unitCodeIds;

    @ApiModelProperty(value = "国家征信")
    private Integer nationalCredit;

    @ApiModelProperty(value = "海关征信")
    private Integer customsCredit;

    @ApiModelProperty(value = "海关信用等级(0:一般认证企业,1:一般信用企业,2:高级信用企业,3:失信企业)")
    private Integer customsCreditRating;


    public void checkAddr(){
        if (this.nationalCredit==null) {
            throw new JayudBizException(400,"请输入国家企业信用信息公示系统查询结果");
        }
        if (this.customsCredit==null) {
            throw new JayudBizException(400,"请输入中国海关企业进出口信用公示平台查询结果");
        }
    }
}
