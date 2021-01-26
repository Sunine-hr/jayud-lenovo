package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 供应商信息
 * </p>
 *
 * @author 李达荣
 * @since 2020-10-29
 */
@Data
public class AddSupplierInfoForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID,修改时必传")
    private Long id;

    @ApiModelProperty(value = "供应商代码", required = true)
    @NotEmpty(message = "supplierCode is required")
    private String supplierCode;

    @ApiModelProperty(value = "服务类型id集合", required = true)
    @NotNull(message = "productClassifyIds is required")
    private List<Long> productClassifyIds;

    @ApiModelProperty(value = "供应商名称(中)", required = true)
    @NotEmpty(message = "supplierChName is required")
    private String supplierChName;

    @ApiModelProperty(value = "联系人", required = true)
    @NotEmpty(message = "contacts is required")
    private String contacts;

    @ApiModelProperty(value = "联系电话", required = true)
    @NotEmpty(message = "contactNumber is required")
    private String contactNumber;

    @ApiModelProperty(value = "地址", required = true)
    @NotEmpty(message = "address is required")
    private String address;

    @ApiModelProperty(value = "结算类型（0:票结,1:周结,2:月结,3:票后,4:预充值）", required = true)
    @NotEmpty(message = "settlementType is required")
    private String settlementType;

    @ApiModelProperty(value = "账期", required = true)
    @NotEmpty(message = "paymentDay is required")
    private String paymentDay;

    @ApiModelProperty(value = "税票种类型", required = true)
    @NotEmpty(message = "taxReceipt is required")
    private String taxReceipt;

    @ApiModelProperty(value = "税率", required = true)
    @NotEmpty(message = "rate is required")
    private String rate;

    @ApiModelProperty(value = "采购员id", required = true)
//    @NotNull(message = "buyerId is required")
    private Long buyerId;

    @ApiModelProperty(value = "法人主体id", required = true)
    @NotEmpty(message = "legal_entity_id is required")
    private Long legalEntityId;

}
