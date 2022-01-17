package com.jayud.scm.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 商品操作日志表
 * </p>
 *
 * @author LLJ
 * @since 2021-07-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AddCommodityFollowForm {

    @ApiModelProperty(value = "商品ID")
    private Integer commodityId;

    @ApiModelProperty(value = "操作内容")
    @NotNull(message = "内容不为空")
    private String followContext;

    @ApiModelProperty(value = "备注")
    private String remark;

}
