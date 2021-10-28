package com.jayud.oms.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.enums.SubOrderSignEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.model.po.ContractQuotationDetails;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    @ApiModelProperty(value = "客户code")
    private String customerCode;

    @ApiModelProperty(value = "客户id")
    private Long customerId;

    @ApiModelProperty(value = "客户名称")
    private String customerName;


    @ApiModelProperty(value = "合同编号集合")
    private List<String> contractNos;

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

    @ApiModelProperty(value = "中港运输报价详情")
    private List<ContractQuotationDetailsVO> tmsDetails = new ArrayList<>();

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


    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
        if (!StringUtils.isEmpty(contractNo)) {
            String[] split = contractNo.split(",");
            contractNos = Arrays.asList(split);
        }
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public void assembleDetails(List<ContractQuotationDetails> details, Map<String, List<InitComboxVO>> costType) {
        for (ContractQuotationDetails detail : details) {
            switch (SubOrderSignEnum.getEnum(detail.getSubType())) {
                case ZGYS:
                    ContractQuotationDetailsVO convert = ConvertUtil.convert(detail, ContractQuotationDetailsVO.class);
                    convert.setCategorys(costType.get(convert.getCostCode()));
                    tmsDetails.add(convert);
                    break;
            }
        }

    }
}
