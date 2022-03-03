package com.jayud.auth.model.dto;

import com.jayud.auth.model.po.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ciro
 * @date 2022/3/3 9:46
 * @description:
 */
@Data
@ApiModel(value = "后台用户对象", description = "后台用户对象")
public class SysUserDTO extends SysUser {

    /**
     * 部门中文集合
     */
    @ApiModelProperty(value = "部门中文集合")
    private String departNames;
}
