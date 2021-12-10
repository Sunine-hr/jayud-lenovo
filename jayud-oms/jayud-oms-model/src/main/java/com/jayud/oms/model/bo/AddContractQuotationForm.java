package com.jayud.oms.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.jayud.common.enums.ContractQuotationModeEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.FileView;
import com.jayud.common.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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

    @ApiModelProperty(value = "客户/供应商code")
    private String customerCode;

    @ApiModelProperty(value = "合同编号")
    private List<String> contractNos;

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

    @ApiModelProperty(value = "流程状态(1:未提交,2:待部门经理审核,3:待公司法务审核,4:待总审核,5:未通过,6:待完善,7:已完成)")
    private Integer optStatus = 1;

    @ApiModelProperty(value = "中港")
    private List<AddContractQuotationDetailsForm> tmsDetails;

    @ApiModelProperty(value = "报关")
    private List<AddContractQuotationDetailsForm> bgDetails;

    @ApiModelProperty(value = "香港配送")//备注:前端要改成这个名字
    private List<AddContractQuotationDetailsForm> xgDetails;

    @ApiModelProperty(value = "接单法人id")
    private Long legalEntityId;

    @ApiModelProperty(value = "合同对象(1:客户,2:供应商)")
    private Integer type;

    @ApiModelProperty(value = "合同报价文件")
    private List<FileView> files;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

//    public void setTmsDetails(List<AddContractQuotationDetailsForm> tmsDetails) {
//        this.tmsDetails = tmsDetails;
//        if (CollectionUtils.isEmpty(tmsDetails)) {
//            this.tmsDetails = null;
//        }
//    }

    public void checkParam() {
        if (StringUtils.isEmpty(number)) {
            throw new JayudBizException(400, "报价编号不能为空");
        }
        if (customerCode == null) {
            throw new JayudBizException(400, "客户不能为空");
        }
//        if (StringUtils.isEmpty(name)) {
//            throw new JayudBizException(400, "报价名称不能为空");
//        }
        if (startTime == null) {
            throw new JayudBizException(400, "有效起始时间不能为空");
        }
        if (endTime == null) {
            throw new JayudBizException(400, "有效结束时间不能为空");
        }

        if (endTime.compareTo(startTime) < 0) {
            throw new JayudBizException(400, "结束时间不能小于开始时间");
        }
        if (legalEntityId == null) {
            throw new JayudBizException(400, "请输入公司法人");
        }
        if (CollectionUtils.isEmpty(tmsDetails)
                && CollectionUtils.isEmpty(bgDetails)
                && CollectionUtils.isEmpty(xgDetails)) {
            throw new JayudBizException(400, "请填写合同报价费用");
        }
        if (!CollectionUtils.isEmpty(tmsDetails)) {
            tmsDetails.forEach(e -> {
                e.setSubType(ContractQuotationModeEnum.ZGYS.getCode());
                e.checkParam();
            });
        }
        if (!CollectionUtils.isEmpty(bgDetails)) {
            bgDetails.forEach(e -> {
                e.setSubType(ContractQuotationModeEnum.BG.getCode());
                e.checkParam();
            });
        }

        if (!CollectionUtils.isEmpty(xgDetails)) {
            xgDetails.forEach(e -> {
                e.setSubType(ContractQuotationModeEnum.HKPS.getCode());
                e.checkParam();
            });
        }

    }

    public void assemblyContractNo() {
        if (!CollectionUtils.isEmpty(contractNos)) {
            StringBuilder sb = new StringBuilder();
            contractNos.forEach(e -> {
                sb.append(e).append(",");
            });
            this.contractNo = sb.toString();
        }
    }

    public void checkCostDuplicate() {
        doCheckCostDuplicate(tmsDetails);
    }

    public void doCheckCostDuplicate(List<AddContractQuotationDetailsForm> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        Map<String, List<AddContractQuotationDetailsForm>> listMap = list.stream().collect(Collectors.groupingBy(e -> e.getVehicleSize() + "~" + e.getCostCode() + "~" + e.getCostTypeId()));
//        Map<String, Long> count = list.stream().collect(Collectors.groupingBy(e -> e.getStartingPlace()
//                + "~" + e.getDestinationId() + "~" + e.getVehicleSize() + "~" + e.getCostCode() + "~" + e.getCostTypeId(), Collectors.counting()));
//        count.forEach((k, v) -> {
//            if (v > 1) throw new JayudBizException("已存在相同费用,请检查");
//        });
        listMap.forEach((k, v) -> {
            for (int i = 0; i < v.size(); i++) {
                AddContractQuotationDetailsForm tmp = v.get(i);
                for (int i1 = 0; i1 < v.size(); i1++) {
                    AddContractQuotationDetailsForm form = v.get(i1);
                    if (i1 == i) {
                        continue;
                    }
                    List<Long> startPlaceIds = tmp.getStartingPlaceIds().stream().filter(e -> form.getStartingPlaceIds().contains(e)).collect(Collectors.toList());
                    if (startPlaceIds.size() == 0) {
                        continue;
                    }
                    List<Long> endPlaceIds = tmp.getDestinationIds().stream().filter(e -> form.getDestinationIds().contains(e)).collect(Collectors.toList());
                    if (endPlaceIds.size() > 0) {
                        throw new JayudBizException("已存在相同费用,请检查");
                    }
                }
            }
        });
    }
}
