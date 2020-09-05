package com.jayud.model.po;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Company对象", description="公司表")
@Data
public class Company {

    @TableId(value = "id")
    private Long id;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

}
