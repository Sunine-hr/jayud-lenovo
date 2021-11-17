package com.jayud.oms.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
public class QueryContractQuotationForm extends BasePageForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "报价名称")
    private String name;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "流程状态(1:未提交,2:待部门经理审核,3:待公司法务审核,4:待总审核,5:未通过,6:待完善,7:已完成)")
    private Integer optStatus;

    @ApiModelProperty(value = "标记(1:已到期,2:即将到期,3:无标记)")
    private Integer sign;

}
