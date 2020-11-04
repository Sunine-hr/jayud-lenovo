package com.jayud.oms.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.time.LocalDateTime;
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
public class SupplierInfoVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增ID")
    private Long id;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "服务类型")
    private String productClassify;

    @ApiModelProperty(value = "服务类型id集合")
    private List<Long> productClassifyIds;

    @ApiModelProperty(value = "供应商名称(中)")
    private String supplierChName;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    private String contactNumber;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "结算类型")
    private String settlementType;

    @ApiModelProperty(value = "账期")
    private String paymentDay;

    @ApiModelProperty(value = "税票种类型")
    private String taxReceipt;

    @ApiModelProperty(value = "税率")
    private String rate;

    @ApiModelProperty(value = "采购员id")
    private Long buyerId;

    @ApiModelProperty(value = "采购员")
    private String buyer;

    @ApiModelProperty(value = "审核状态")
    private String auditStatus;

    @ApiModelProperty(value = "审核描述")
    private String auditComment;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;


    /**
     * 组装服务类型id集合
     */
    public void packageProductClassifyId(String ids) {
        if (StringUtils.isEmpty(ids)) {
            return;
        }
        this.productClassifyIds = new ArrayList<>();
        for (String id : ids.split(",")) {
            this.productClassifyIds.add(Long.parseLong(id));
        }
    }
}
