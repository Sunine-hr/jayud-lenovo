package com.jayud.scm.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 香港抬头表
 * </p>
 *
 * @author LLJ
 * @since 2021-08-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "BHkCompany对象", description = "香港抬头表")
public class BHkCompany extends Model<BHkCompany> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自动ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "中文名称")
    private String companyName;

    @ApiModelProperty(value = "英文名称")
    private String companyNameEn;

    @ApiModelProperty(value = "中文地址")
    private String address;

    @ApiModelProperty(value = "英文地址")
    private String addressEn;

    @ApiModelProperty(value = "联系人")
    private String relationer;

    @ApiModelProperty(value = "电话")
    private String tel;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "传真")
    private String fax;

    @ApiModelProperty(value = "纳税号")
    private String taxNo;

    @ApiModelProperty(value = "开户行")
    private String bankName;

    @ApiModelProperty(value = "银行账号")
    private String bankNum;

    @ApiModelProperty(value = "客户财务编号")
    private String cEasNo;

    @ApiModelProperty(value = "供应商财务编号")
    private String sEasNo;

    @ApiModelProperty(value = "海关代码")
    private String hsCode;

    @ApiModelProperty(value = "注册登记号/信用代码")
    private String creditCode;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人ID")
    private Integer crtBy;

    @ApiModelProperty(value = "创建人名称")
    private String crtByName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime crtByDtm;

    @ApiModelProperty(value = "最后修改人ID")
    private Integer mdyBy;

    @ApiModelProperty(value = "最后修改人名称")
    private String mdyByName;

    @ApiModelProperty(value = "最后修改时间")
    private LocalDateTime mdyByDtm;

    @ApiModelProperty(value = "删除标记")
    private Integer voided;

    @ApiModelProperty(value = "删除人ID")
    private Integer voidedBy;

    @ApiModelProperty(value = "删除人名称")
    private String voidedByName;

    @ApiModelProperty(value = "删除时间")
    private LocalDateTime voidedByDtm;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
