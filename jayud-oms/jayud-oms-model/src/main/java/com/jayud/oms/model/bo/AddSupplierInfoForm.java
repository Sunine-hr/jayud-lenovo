package com.jayud.oms.model.bo;

import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
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

    @ApiModelProperty(value = "法人主体id集合", required = true)
    @NotNull(message = "legalEntityId is required")
    private List<Long> legalEntityIds;

    @ApiModelProperty(value = "国家征信")
    private Integer nationalCredit;

    @ApiModelProperty(value = "海关征信")
    private Integer customsCredit;

    @ApiModelProperty(value = "海关信用等级(0:一般认证企业,1:一般信用企业,2:高级信用企业,3:失信企业)")
    private Integer customsCreditRating;

    @ApiModelProperty(value = "是否高级认证")
    private Boolean isAdvancedCertification;

    @ApiModelProperty(value = "附件集合")
    private List<FileView> fileViews = new ArrayList<>();

    public void checkAddr() {
        if (this.nationalCredit == null) {
            throw new JayudBizException(400, "请输入国家企业信用信息公示系统查询结果");
        }
        if (this.customsCredit == null) {
            throw new JayudBizException(400, "请输入中国海关企业进出口信用公示平台查询结果");
        }
//        if (this.isAdvancedCertification == null) {
//            throw new JayudBizException(400, "高级认证不能为空");
//        }

    }

}
