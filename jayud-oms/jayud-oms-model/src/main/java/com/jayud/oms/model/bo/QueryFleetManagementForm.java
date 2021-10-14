package com.jayud.oms.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 车队管理
 * </p>
 *
 * @author LDR
 * @since 2021-10-13
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "FleetManagement对象", description = "车队管理")
public class QueryFleetManagementForm extends BasePageForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "车队编号")
    private String code;

    @ApiModelProperty(value = "车队名称")
    private String name;

    @ApiModelProperty(value = "状态(0:禁用 1:启用, 2:删除)")
    private List<Integer> status;
}
