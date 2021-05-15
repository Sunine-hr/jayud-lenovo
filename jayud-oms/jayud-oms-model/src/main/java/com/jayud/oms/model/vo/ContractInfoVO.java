package com.jayud.oms.model.vo;

import com.jayud.common.utils.FileView;
import com.jayud.oms.model.po.ProductClassify;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class ContractInfoVO {

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "客户/供应商名称")
    private String name;

    @ApiModelProperty(value = "合同编号")
    private String contractNo;

    @ApiModelProperty(value = "合同地址")
    private String contractUrl;

    @ApiModelProperty(value = "合同附件名称")
    private String contractName;

    @ApiModelProperty(value = "合同地址集合")
    private List<FileView> fileViews = new ArrayList<>();

    @ApiModelProperty(value = "业务类型")
    private String businessType;

    @ApiModelProperty(value = "业务类型集合")
    private List<Long> businessTypes = new ArrayList<>();

    @ApiModelProperty(value = "法人主体")
    private Long legalEntity;

    @ApiModelProperty(value = "法人主体名称")
    private String legalEntityName;

    @ApiModelProperty(value = "合同起期")
    private String startDate;

    @ApiModelProperty(value = "合同止期")
    private String endDate;

    @ApiModelProperty(value = "创建人")
    private String createdUser;

    @ApiModelProperty(value = "创建时间")
    private String createdTimeStr;

    @ApiModelProperty(value = "合同类型 0:客户合同 1:供应商合同")
    private String type;

    @ApiModelProperty(value = "合同绑定的业务id")
    private Long bindId;

    @ApiModelProperty(value = "备注")
    private String remarks;

    public void setBusinessTypes(String businessType) {
        if (businessType != null) {
            String[] strList = businessType.split(",");
            for (String str : strList) {
                businessTypes.add(Long.parseLong(str));
            }
        }
    }

    /**
     * 构建合同业务类型
     */
    public void buildViewBusinessType(List<ProductClassify> productBizs) {
        StringBuilder sb = new StringBuilder();
        for (Long str : businessTypes) {
            for (ProductClassify productClassify : productBizs) {
                if (str.equals(productClassify.getId())) {
                    sb.append(productClassify.getName() + ",");
                }
            }
        }
        if (!"".equals(String.valueOf(sb))) {
            businessType = sb.substring(0, sb.length() - 1);
        }
    }

}
