package com.jayud.oms.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 绑定消息模板
 * </p>
 *
 * @author LDR
 * @since 2021-08-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "BindingMsgTemplate对象", description = "绑定消息模板")
public class BindingMsgTemplateInfoVO extends Model<BindingMsgTemplateInfoVO> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "消息列表id")
    private Long msgListId;

    @ApiModelProperty(value = "岗位")
    private String post;

    @ApiModelProperty(value = "模板id")
    private Long templateId;

    @ApiModelProperty(value = "消息类型(1:操作状态,2:客户状态)")
    private Integer type;

    @ApiModelProperty(value = "接收方式")
    private String receivingMode;

    @ApiModelProperty(value = "接收账号")
    private String receivingAccount;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "状态(0禁用 1启用)")
    private Integer status;

    @ApiModelProperty(value = "是否关注自己")
    private Boolean selfRegarding;

    @ApiModelProperty(value = "消息渠道(邮件,微信)")
    private String msgChannel;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
