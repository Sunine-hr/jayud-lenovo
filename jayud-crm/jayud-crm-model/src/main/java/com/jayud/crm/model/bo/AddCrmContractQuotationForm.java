package com.jayud.crm.model.bo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.jayud.common.entity.SysBaseEntity;
import com.jayud.common.enums.ContractQuotationModeEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.FileView;
import com.jayud.common.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * CrmContractQuotation 实体类
 *
 * @author jayud
 * @since 2022-03-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "合同报价对象", description = "合同报价")
public class AddCrmContractQuotationForm extends SysBaseEntity {


    @ApiModelProperty(value = "报价编号(合同报价编号)")
    private String number;

    @ApiModelProperty(value = "客户/供应商id")
    private String customerId;

    @ApiModelProperty(value = "客户/供应商code")
    private String customerCode;

    @ApiModelProperty(value = "客户/供应商名称")
    private String customerName;

    @ApiModelProperty(value = "报价名称")
    private String name;

    @ApiModelProperty(value = "有效起始时间")
    private LocalDate startTime;

    @ApiModelProperty(value = "有效结束时间")
    private LocalDate endTime;

    @ApiModelProperty(value = "状态（0禁用 1启用）")
    private Integer status;

    @ApiModelProperty(value = "合同对象(1:客户,2:供应商)")
    private Integer type = 1;

    @ApiModelProperty(value = "法人主体名称id")
    private Long legalEntityId;

    @ApiModelProperty(value = "法人主体")
    private Long legalEntity;

    @ApiModelProperty(value = "销售员id")
    private Long userId;

    @ApiModelProperty(value = "销售员名称")
    private String user;

    @ApiModelProperty(value = "中港")
    private List<AddCrmContractQuotationDetailsForm> tmsDetails;

    @ApiModelProperty(value = "报关")
    private List<AddCrmContractQuotationDetailsForm> bgDetails;

    @ApiModelProperty(value = "香港配送")//备注:前端要改成这个名字
    private List<AddCrmContractQuotationDetailsForm> xgDetails;


    @ApiModelProperty(value = "审核级别")
    private Integer fLevel;

    @ApiModelProperty(value = "当前级别")
    private Integer fStep;

    @ApiModelProperty(value = "审核状态")
    private String checkStateFlag;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;

    @ApiModelProperty(value = "合同报价文件")
    private List<FileView> files;

    /**
     * 检查参数
     */
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

    public void checkCostDuplicate() {
        doCheckCostDuplicate(tmsDetails);
    }

    /**
     * 执行检查重复费用
     * @param list
     */
    public void doCheckCostDuplicate(List<AddCrmContractQuotationDetailsForm> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        Map<String, List<AddCrmContractQuotationDetailsForm>> listMap = list.stream().collect(Collectors.groupingBy(e -> e.getVehicleSize() + "~" + e.getCostCode() + "~" + e.getCostTypeId()));
//        Map<String, Long> count = list.stream().collect(Collectors.groupingBy(e -> e.getStartingPlace()
//                + "~" + e.getDestinationId() + "~" + e.getVehicleSize() + "~" + e.getCostCode() + "~" + e.getCostTypeId(), Collectors.counting()));
//        count.forEach((k, v) -> {
//            if (v > 1) throw new JayudBizException("已存在相同费用,请检查");
//        });
        listMap.forEach((k, v) -> {
            for (int i = 0; i < v.size(); i++) {
                AddCrmContractQuotationDetailsForm tmp = v.get(i);
                for (int i1 = 0; i1 < v.size(); i1++) {
                    AddCrmContractQuotationDetailsForm form = v.get(i1);
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
