package com.jayud.model.vo;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.model.enums.SystemUserStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author bocong.zheng
 */
@Data
public class SystemUserVO {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String name;

    @ApiModelProperty(value = "头像")
    private String icon;

    @ApiModelProperty(value = "联系方式，邮箱")
    private String contract;

    @ApiModelProperty(value = "备注信息")
    private String note;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "最后登录时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime loginTime;

    @ApiModelProperty(value = "帐号启用状态：0->Off；1->On")
    private Integer status;

    @ApiModelProperty(value = "国家")
    private String country;

    @ApiModelProperty(value = "州/省")
    private String state;

    @ApiModelProperty(value = "时区")
    private Integer timeZone;

    @ApiModelProperty(value = "图片(支持多张)")
    private String photo;

    @ApiModelProperty(value = "用户角色")
    private List<SystemRoleVO> roles;

    @ApiModelProperty(value = "角色ID集合")
    private List<Long> roleIds;

    @ApiModelProperty(value = "状态描述")
    private String statusDesc;

    @ApiModelProperty(value = "菜单树")
    private List<SystemMenuNode> menuNodeList;

    public String getStatusDesc(){
        return SystemUserStatusEnum.getDesc(this.status);
    }

    public List<Long> getRoleIds(){
        if(CollectionUtils.isNotEmpty(roles)){
            return roles.stream().map(e->e.getId()).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
