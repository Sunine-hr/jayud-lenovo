package com.jayud.oms.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 合同报价
 * </p>
 *
 * @author LDR
 * @since 2021-10-26
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ContractQuotation对象", description="合同报价")
public class ContractQuotation extends Model<ContractQuotation> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "报价编号")
    private String number;

    @ApiModelProperty(value = "客户code")
    private String customerCode;

    @ApiModelProperty(value = "合同编号")
    private String contractNo;

    @ApiModelProperty(value = "报价名称")
    private String name;

    @ApiModelProperty(value = "有效起始时间")
    private LocalDate startTime;

    @ApiModelProperty(value = "有效结束时间")
    private LocalDate endTime;

//    @ApiModelProperty(value = "审核状态(1:已审核,2:未审核)")
//    private Integer auditStatus;

    @ApiModelProperty(value = "状态（0禁用 1启用 2删除）")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "描述")
    private String remarks;

    @ApiModelProperty(value = "合同对象(1:客户,2:供应商)")
    private Integer type;

    @ApiModelProperty(value = "流程状态(1:未提交,2:待部门经理审核,3:待公司法务审核,4:待总审核,5:未通过,6:待完善,7:已完成)")
    private Integer optStatus;

    @ApiModelProperty(value = "接单法人id")
    private Long legalEntityId;

    @ApiModelProperty(value = "未通过消息")
    private String reasonsFailure;

    @ApiModelProperty(value = "合同报价附件")
    private String file;

    @ApiModelProperty(value = "合同报价附件名称")
    private String fileName;

    @ApiModelProperty(value = "签署合同附件")
    private String signContractFile;

    @ApiModelProperty(value = "签署合同附件名称")
    private String signContractFileName;

    @ApiModelProperty(value = "签署报价附件")
    private String signOfferFile;

    @ApiModelProperty(value = "签署报价附件名称")
    private String signOfferFileName;

    @ApiModelProperty(value = "法务审核")
    private String legalAudit;

    @ApiModelProperty(value = "部门经理审核")
    private String depManagerReview;

    @ApiModelProperty(value = "总经理审核")
    private String generalManagerReview;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
