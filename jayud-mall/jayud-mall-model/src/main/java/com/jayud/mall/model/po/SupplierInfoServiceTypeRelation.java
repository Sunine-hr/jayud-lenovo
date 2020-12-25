package com.jayud.mall.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 供应商-服务类型关联表
 * </p>
 *
 * @author fachang.mao
 * @since 2020-12-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SupplierInfoServiceTypeRelation对象", description="供应商-服务类型关联表")
public class SupplierInfoServiceTypeRelation extends Model<SupplierInfoServiceTypeRelation> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "供应商id(supplier_info id)", position = 2)
    private Long infoId;

    @ApiModelProperty(value = "服务类id(supplier_service_type id)", position = 3)
    private Long serviceTypeId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
