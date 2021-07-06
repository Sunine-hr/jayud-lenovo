package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author mfc
 */
@Data
public class SaveSystemUserForm {

    @ApiModelProperty(value = "用户ID 修改时必传", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "登录名,用户名", position = 2)
    @JSONField(ordinal = 2)
    private String name;

    @ApiModelProperty(value = "所属公司(直接输入名称)", position = 3)
    @JSONField(ordinal = 3)
    private String company;

    @ApiModelProperty(value = "工号", position = 4)
    @JSONField(ordinal = 4)
    private String wkno;

    @ApiModelProperty(value = "用户姓名(中文名)", position = 4)
    private String userName;

    @ApiModelProperty(value = "英文名", position = 5)
    @JSONField(ordinal = 5)
    private String enUserName;

    @ApiModelProperty(value = "手机号(联系方式)", position = 6)
    @JSONField(ordinal = 6)
    private String phone;

    @ApiModelProperty(value = "邮箱", position = 7)
    @JSONField(ordinal = 7)
    private String email;

    @ApiModelProperty(value = "用户角色id，List<long>", position = 8)
    @JSONField(ordinal = 8)
    @NotEmpty(message = "用户角色不能为空")
    private List<Long> roleIds;


}
