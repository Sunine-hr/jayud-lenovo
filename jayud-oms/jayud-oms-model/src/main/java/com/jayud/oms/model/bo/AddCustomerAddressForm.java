package com.jayud.oms.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
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
public class AddCustomerAddressForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "地址id,修改时必传")
    private Long id;

    @ApiModelProperty(value = "地址类型（0 提货地址 1送货地址）")
    @NotEmpty(message = "type is required")
    private String type;

    @ApiModelProperty(value = "客户地址id")
    @NotNull(message = "customerId is required")
    private Long customerId;

    @ApiModelProperty(value = "联系人")
    @NotEmpty(message = "contacts is required")
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    @NotEmpty(message = "phone is required")
    private String phone;

    @ApiModelProperty(value = "省主键")
    @NotNull(message = "province is required")
    private Integer province;

    @ApiModelProperty(value = "市主键")
    @NotNull(message = "city is required")
    private Integer city;

    @ApiModelProperty(value = "区主键")
    private Integer area;

    @ApiModelProperty(value = "详细地址")
    @NotEmpty(message = "address is required")
    private String address;

    @ApiModelProperty(value = "邮编")
    private String postCode;

    @ApiModelProperty(value = "传真")
    private String fax;

    @ApiModelProperty(value = "邮箱")
    private String mailbox;


}
