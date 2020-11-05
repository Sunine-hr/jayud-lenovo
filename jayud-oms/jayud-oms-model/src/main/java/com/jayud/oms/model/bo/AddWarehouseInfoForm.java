package com.jayud.oms.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.jayud.oms.model.po.VehicleInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 仓库信息表
 * </p>
 *
 * @author
 * @since 2020-11-05
 */
@Data
public class AddWarehouseInfoForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "中转仓库代码")
    @NotEmpty(message = "warehouseCode is required")
    private String warehouseCode;

    @ApiModelProperty(value = "中转仓库名称")
    @NotEmpty(message = "warehouseName is required")
    private String warehouseName;

    @ApiModelProperty(value = "联系人")
    @NotEmpty(message = "contacts is required")
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    @NotEmpty(message = "contactNumber is required")
    private String contactNumber;

    @ApiModelProperty(value = "地址")
    @NotEmpty(message = "address is required")
    private String address;

    @ApiModelProperty(value = "省")
    @NotEmpty(message = "province is required")
    private String province;

    @ApiModelProperty(value = "市")
    @NotEmpty(message = "city is required")
    private String city;

    @ApiModelProperty(value = "区")
    @NotEmpty(message = "area is required")
    private String area;

    @ApiModelProperty(value = "邮编")
    private String zipCode;

}
