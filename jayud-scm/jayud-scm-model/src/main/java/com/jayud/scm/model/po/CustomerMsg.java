package com.jayud.scm.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 客户消息通知表
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="CustomerMsg对象", description="客户消息通知表")
public class CustomerMsg extends Model<CustomerMsg> {

    private static final long serialVersionUID = 1L;

      private Integer id;

    @ApiModelProperty(value = "客户id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer customerId;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "消息模板id")
    private Integer msgTemplateId;

    private String temp01;

    private String temp02;

    private String temp03;

    private String temp04;

    private String temp05;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否禁用 0 未禁用 1 已禁用")
    private Integer isDisabled;

    @ApiModelProperty(value = "模板类型，0短信，1邮件，2微信")
    private Integer tplType;

    @ApiModelProperty(value = "客户手机")
    private String msgTel;

    @ApiModelProperty(value = "客户邮箱")
    private String msgMail;

    @ApiModelProperty(value = "消息模板编码")
    private String msgTemplateNo;

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
