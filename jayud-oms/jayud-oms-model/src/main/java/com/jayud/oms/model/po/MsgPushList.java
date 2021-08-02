package com.jayud.oms.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 消息推送列表
 * </p>
 *
 * @author LDR
 * @since 2021-08-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="MsgPushList对象", description="消息推送列表")
public class MsgPushList extends Model<MsgPushList> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "接收人名称")
    private String recipientName;

    @ApiModelProperty(value = "接收人id")
    private Long recipientId;

    @ApiModelProperty(value = "岗位")
    private String post;

    @ApiModelProperty(value = "接收状态")
    private String receivingStatus;

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


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
