package com.jayud.scm.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 角色审核级别权限
 * </p>
 *
 * @author LLJ
 * @since 2021-08-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SystemRoleActionCheckVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "权限名称")
    private String actionName;

    @ApiModelProperty(value = "角色")
    private String name;

    @ApiModelProperty(value = "菜单名称")
    private String title;

    @ApiModelProperty(value = "审核CODE")
    private String actionCode;

    @ApiModelProperty(value = "审核级别")
    private Integer checkLevel;

    @ApiModelProperty(value = "权限最高金额,0不限制")
    private BigDecimal checkMoney;

    @ApiModelProperty(value = "创建人名称")
    private String crtByName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime crtByDtm;

}
