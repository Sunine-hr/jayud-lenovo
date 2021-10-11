package com.jayud.oms.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 油卡管理
 * </p>
 *
 * @author LDR
 * @since 2021-10-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "OilCardManagement对象", description = "油卡管理")
public class QueryOilCardManagementForm extends BasePageForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "油卡卡号")
    private String oilCardNum;

    @ApiModelProperty(value = "油卡名称")
    private String oilName;


}
