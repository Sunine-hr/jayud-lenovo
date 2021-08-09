package com.jayud.scm.model.vo;

import com.jayud.scm.model.po.LegalEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bocong.zheng
 */
@Data
public class SystemUserSimpleVO {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "登录名")
    private String name;

    @ApiModelProperty(value = "用户名")
    private String userName;
}
