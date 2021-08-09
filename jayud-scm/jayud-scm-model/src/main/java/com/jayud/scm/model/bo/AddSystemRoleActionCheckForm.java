package com.jayud.scm.model.bo;

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
import java.util.List;

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
public class AddSystemRoleActionCheckForm {

    @ApiModelProperty(value = "自动ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "角色ID")
    private Integer roleId;

    @ApiModelProperty(value = "权限ID")
    private List<Integer> actionId;

    @ApiModelProperty(value = "审核级别")
    private Integer checkLevel;

    @ApiModelProperty(value = "权限最高金额,0不限制")
    private BigDecimal checkMoney;


}
