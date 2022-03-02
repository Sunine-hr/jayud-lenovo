package com.jayud.crm.model.vo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.jayud.common.entity.InitComboxVO;
import com.jayud.common.entity.SysBaseEntity;
import com.jayud.common.enums.ContractQuotationModeEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.crm.model.po.CrmContractQuotationDetails;
import com.jayud.crm.model.po.CrmFile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * CrmContractQuotation 实体类
 *
 * @author jayud
 * @since 2022-03-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "合同报价对象", description = "合同报价")
public class CrmContractQuotationVO extends SysBaseEntity {


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
    private Integer type;

    @ApiModelProperty(value = "法人主体名称id")
    private Long legalEntityId;

    @ApiModelProperty(value = "法人主体")
    private String legalEntity;

    @ApiModelProperty(value = "销售员id")
    private Long userId;

    @ApiModelProperty(value = "销售员名称")
    private String user;

    @ApiModelProperty(value = "中港运输报价详情")
    private List<CrmContractQuotationDetailsVO> tmsDetails = new ArrayList<>();

    @ApiModelProperty(value = "中港运输报价详情")
    private List<CrmContractQuotationDetailsVO> bgDetails = new ArrayList<>();

    @ApiModelProperty(value = "香港配送")//备注:前端要改成这个名字
    private List<CrmContractQuotationDetailsVO> xgDetails = new ArrayList<>();

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

    @ApiModelProperty(value = "文件集合")
    private List<CrmFile> files;

    @ApiModelProperty(value = "报价有效时间")
    private String effectiveTime;



    public void assembleDetails(List<CrmContractQuotationDetails> details, Map<String, List<InitComboxVO>> costType) {
        for (CrmContractQuotationDetails detail : details) {
            switch (ContractQuotationModeEnum.getEnum(detail.getSubType())) {
                case ZGYS:
                    CrmContractQuotationDetailsVO convert = ConvertUtil.convert(detail, CrmContractQuotationDetailsVO.class);
                    convert.setCategorys(costType.get(convert.getCostCode()));
                    tmsDetails.add(convert);
                    break;
                case BG:
                    convert = ConvertUtil.convert(detail, CrmContractQuotationDetailsVO.class);
                    convert.setCategorys(costType.get(convert.getCostCode()));
                    bgDetails.add(convert);
                    break;
                case HKPS:
                    convert = ConvertUtil.convert(detail, CrmContractQuotationDetailsVO.class);
                    convert.setCategorys(costType.get(convert.getCostCode()));
                    xgDetails.add(convert);
                    break;
            }
        }

    }

}
