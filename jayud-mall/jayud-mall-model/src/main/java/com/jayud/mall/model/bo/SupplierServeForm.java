package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.mall.model.vo.SupplierCostVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SupplierServeForm {

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
    @JSONField(ordinal = 6, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime effectiveDate;

    @ApiModelProperty(value = "失效日期", position = 7)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 7, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiryDate;

    /*供应商服务费用list*/
    @ApiModelProperty(value = "供应商服务费用list", position = 8)
    @JSONField(ordinal = 8)
    private List<SupplierCostVO> supplierCostVOList;

}
