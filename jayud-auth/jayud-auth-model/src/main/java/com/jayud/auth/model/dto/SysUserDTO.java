package com.jayud.auth.model.dto;

import com.jayud.auth.model.po.SysUser;
import lombok.Data;

/**
 * @author ciro
 * @date 2022/3/3 9:46
 * @description:
 */
@Data
public class SysUserDTO extends SysUser {

    /**
     * 部门中文集合
     */
    private String departNames;
}
