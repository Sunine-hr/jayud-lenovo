package com.jayud.oms.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.utils.DateUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
@ApiModel(value = "ContractQuotation对象", description = "合同报价")
public class ContractQuotationVO extends Model<ContractQuotationVO> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "报价编号")
    private String number;

    //    @ApiModelProperty(value = "客户code")
//    private String customerCode;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "合同编号")
    private String contractNo;

    @ApiModelProperty(value = "报价名称")
    private String name;

    @ApiModelProperty(value = "有效起始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private LocalDate startTime;

    @ApiModelProperty(value = "有效结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private LocalDate endTime;

    @ApiModelProperty(value = "审核状态(1:已审核,2:未审核)")
    private Integer auditStatus;

    @ApiModelProperty(value = "审核状态描述(1:已审核,2:未审核)")
    private String auditStatusDesc;

    @ApiModelProperty(value = "状态（0禁用 1启用 2删除）")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = DateUtils.DATE_TIME_PATTERN)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
        switch (auditStatus) {
            case 1:
                this.auditStatusDesc = "已审核";
                break;
            case 2:
                this.auditStatusDesc = "未审核";
        }
    }


    //    @ApiModelProperty(value = "更新时间")
//    private LocalDateTime updateTime;
//
//    @ApiModelProperty(value = "更新人")
//    private String updateUser;
//
//    @ApiModelProperty(value = "描述")
//    private String remarks;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
