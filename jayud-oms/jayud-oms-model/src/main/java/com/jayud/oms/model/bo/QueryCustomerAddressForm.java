package com.jayud.oms.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 客户地址
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-04
 */
@Data
public class QueryCustomerAddressForm extends BasePageForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "客户主键")
    @NotNull(message = "客户主键不能为空")
    private Long customerId;


    @ApiModelProperty(value = "客户code 创建订单用的")
    private String customerCode;

    @ApiModelProperty(value = "状态（0禁用 1启用）")
    private String status;

    @ApiModelProperty(value = "地址类型（0 提货地址 1送货地址）")
    private String type;

}
