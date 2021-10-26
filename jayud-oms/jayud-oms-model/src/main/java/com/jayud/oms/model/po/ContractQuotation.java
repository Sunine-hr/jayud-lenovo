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

/**
 * <p>
 * 合同报价
 * </p>
 *
 * @author LDR
 * @since 2021-10-26
 */
@Data
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

    @ApiModelProperty(value = "审核状态(1:已审核,2:未审核)")
    private Integer auditStatus;

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


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
