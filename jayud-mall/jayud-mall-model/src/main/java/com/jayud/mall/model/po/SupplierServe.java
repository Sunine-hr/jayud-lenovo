package com.jayud.mall.model.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 供应商服务
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SupplierServe对象", description="供应商服务")
public class SupplierServe extends Model<SupplierServe> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
