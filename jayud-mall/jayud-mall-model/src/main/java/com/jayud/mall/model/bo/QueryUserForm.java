package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 查询用户表单
 */
@Data
public class QueryUserForm extends BasePageForm{

    @ApiModelProperty(value = "用户名", position = 1)
    @JSONField(ordinal = 1)
    private String name;

    @ApiModelProperty(value = "工号", position = 2)
    @JSONField(ordinal = 2)
    private String wkno;

    @ApiModelProperty(value = "帐号启用状态：0->Off 启用；1->On 停用", position = 2)
    @JSONField(ordinal = 3)
    private Integer status;

    //角色-暂未关联
    @ApiModelProperty(value = "用户角色id，List<long>", position = 4)
    @JSONField(ordinal = 4)
    private List<Long> roleIds;



}
