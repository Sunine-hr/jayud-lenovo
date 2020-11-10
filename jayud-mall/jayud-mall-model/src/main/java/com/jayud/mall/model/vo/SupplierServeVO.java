package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SupplierServeVO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "服务代码")
    private String serveCode;

    @ApiModelProperty(value = "服务名")
    private String serveName;

    @ApiModelProperty(value = "1海运 2空运 3陆运")
    private Integer transportPay;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

    @ApiModelProperty(value = "创建人")
    private Integer userId;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "生效日期")
    private LocalDateTime effectiveDate;

    @ApiModelProperty(value = "失效日期")
    private LocalDateTime expiryDate;

    /*供应商服务费用list*/
    @ApiModelProperty(value = "供应商服务费用list")
    private List<SupplierCostVO> supplierCostVOList;


}
