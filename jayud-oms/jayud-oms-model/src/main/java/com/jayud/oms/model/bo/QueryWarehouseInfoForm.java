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
public class QueryWarehouseInfoForm extends BasePageForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "中转仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    private String contactNumber;

    @ApiModelProperty("状态 0 禁用 1启用")
    private String status;
}
