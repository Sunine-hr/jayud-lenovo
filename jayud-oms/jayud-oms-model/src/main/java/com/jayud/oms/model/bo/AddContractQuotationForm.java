package com.jayud.oms.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.StringUtils;
import com.jayud.common.utils.Utilities;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
@ApiModel(value = "ContractQuotation对象", description = "合同报价")
public class AddContractQuotationForm extends Model<AddContractQuotationForm> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "报价编号")
    private String number;

    @ApiModelProperty(value = "客户id")
    private String customerId;

    @ApiModelProperty(value = "合同编号")
    private List<String> contractNos;

    @ApiModelProperty(value = "报价名称")
    private String name;

    @ApiModelProperty(value = "有效起始时间")
    private LocalDate startTime;

    @ApiModelProperty(value = "有效结束时间")
    private LocalDate endTime;

    @ApiModelProperty(value = "运输")
    private List<AddContractQuotationDetailsForm> tmsDetails;

    public static void main(String[] args) {
        System.out.println(Utilities.printCheckCode(AddContractQuotationForm.class));
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public void checkParam() {
        if (StringUtils.isEmpty(number)) {
            throw new JayudBizException(400, "报价编号不能为空");
        }
        if (StringUtils.isEmpty(customerId)) {
            throw new JayudBizException(400, "客户不能为空");
        }
        if (StringUtils.isEmpty(name)) {
            throw new JayudBizException(400, "报价名称不能为空");
        }
        if (startTime == null) {
            throw new JayudBizException(400, "有效起始时间不能为空");
        }
        if (endTime == null) {
            throw new JayudBizException(400, "有效结束时间不能为空");
        }
        if (tmsDetails != null) {
            tmsDetails.forEach(AddContractQuotationDetailsForm::checkParam);
        }

    }

}
