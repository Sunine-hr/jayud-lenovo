package com.jayud.scm.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 结算方案
 * </p>
 *
 * @author LLJ
 * @since 2021-08-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AddFeeModelForm {

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "客户ID")
    private Integer customerId;

    @ApiModelProperty(value = "业务类型")
    private String modelType;

    @ApiModelProperty(value = "模板编号")
    private String modelName;

    @ApiModelProperty(value = "启用日期")
    private LocalDate useDate;

    @ApiModelProperty(value = "是否默认")
    private Integer isDefaultValue;

}
