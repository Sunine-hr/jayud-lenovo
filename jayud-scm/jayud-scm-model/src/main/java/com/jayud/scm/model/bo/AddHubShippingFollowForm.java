package com.jayud.scm.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 出库单跟踪记录表
 * </p>
 *
 * @author LLJ
 * @since 2021-08-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AddHubShippingFollowForm {

    @ApiModelProperty(value = "自动id")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "出库主表ID")
    private Integer shippingId;

    @ApiModelProperty(value = "委托订单ID")
    private Integer bookingId;

    @ApiModelProperty(value = "跟进类型")
    private String sType;

    @ApiModelProperty(value = "内容")
    private String followContext;

    @ApiModelProperty(value = "备注")
    private String remark;

}
