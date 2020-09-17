package com.jayud.oauth.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Work对象", description="岗位表")
@Data
public class WorkVO {

    private Long id;

    @ApiModelProperty(value = "岗位名称")
    private String workName;

}
