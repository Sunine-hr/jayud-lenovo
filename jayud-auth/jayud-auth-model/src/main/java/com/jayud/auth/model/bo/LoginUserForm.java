package com.jayud.auth.model.bo;

import com.jayud.auth.model.po.SysUser;
import lombok.Data;

import java.util.List;

/**
 * @author ciro
 * @date 2022/2/24 15:54
 * @description: 登录用户信息
 */
@Data
public class LoginUserForm extends SysUser {

    private List<String> roleCodeList;

}
