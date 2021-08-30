package com.jayud.oms.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 客户结算单位
 * </p>
 *
 * @author LDR
 * @since 2021-08-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "CustomerUnit对象", description = "客户结算单位")
public class CustomerUnitVO extends Model<CustomerUnitVO> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "客户id")
    private Long customerId;

    @ApiModelProperty(value = "业务类型")
    private String businessType;

    @ApiModelProperty(value = "业务类型描述")
    private String businessTypeDesc;

    @ApiModelProperty(value = "操作部门Id")
    private Long optDepartmentId;

    @ApiModelProperty(value = "操作部门")
    private String optDepartmentDesc;

    @ApiModelProperty(value = "结算代码code")
    private String unitCode;

    @ApiModelProperty(value = "结算代码")
    private String unitDesc;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
