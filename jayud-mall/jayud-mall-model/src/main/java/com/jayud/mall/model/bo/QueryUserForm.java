package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 查询用户表单
 */
@Data
public class QueryUserForm extends BasePageForm{

    @ApiModelProperty("用户姓名")
    private String userName;

    //工号-无

    //角色-暂未关联

    @ApiModelProperty(value = "帐号启用状态")
    private Integer status;


}
