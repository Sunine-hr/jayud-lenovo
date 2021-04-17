package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SupplierServeVO {

    @ApiModelProperty(value = "主键id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "供应商id(supplier_info id)", position = 2)
    @JSONField(ordinal = 2)
    private Long supplierInfoId;

    @ApiModelProperty(value = "服务类型id(supplier_service_type id)", position = 3)
    @JSONField(ordinal = 3)
    private Long serviceTypeId;

    @ApiModelProperty(value = "服务代码", position = 4)
    @JSONField(ordinal = 4)
    private String serveCode;

    @ApiModelProperty(value = "服务名", position = 5)
    @JSONField(ordinal = 5)
    private String serveName;

    @ApiModelProperty(value = "生效日期", position = 6)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 6)
    private LocalDateTime effectiveDate;

    @ApiModelProperty(value = "失效日期", position = 7)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 7)
    private LocalDateTime expiryDate;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 8)
    @JSONField(ordinal = 8)
    private String status;

    @ApiModelProperty(value = "创建人(system_user id)", position = 9)
    @JSONField(ordinal = 9)
    private Integer userId;

    @ApiModelProperty(value = "用户名(system_user name)", position = 10)
    @JSONField(ordinal = 10)
    private String userName;

    @ApiModelProperty(value = "创建时间", position = 11)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 11)
    private LocalDateTime createTime;

    /*供应商信息supplier_info*/
    @ApiModelProperty(value = "供应商名称", position = 12)
    private String supplierInfoName;

    /*供应商服务类型supplier_service_type*/
    @ApiModelProperty(value = "服务类型名称", position = 13)
    private String serviceTypeName;

    /*供应商服务费用list*/
    @ApiModelProperty(value = "供应商服务费用list", position = 14)
    @JSONField(ordinal = 12)
    private List<SupplierCostVO> supplierCostVOList;


}
