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
import lombok.experimental.Accessors;

/**
 * <p>
 * 消息推送记录
 * </p>
 *
 * @author LDR
 * @since 2021-08-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "MsgPushRecord对象", description = "消息推送记录")
public class MsgPushRecord extends Model<MsgPushRecord> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "接收人名称")
    private String recipientName;

    @ApiModelProperty(value = "接收人id")
    private Long recipientId;

    @ApiModelProperty(value = "消息类型(1:操作状态,2:客户状态)")
    private Integer type;

    @ApiModelProperty(value = "岗位")
    private String post;

    @ApiModelProperty(value = "接收状态")
    private String receivingStatus;

    @ApiModelProperty(value = "接收内容")
    private String receiveContent;

    @ApiModelProperty(value = "接收方式")
    private String receivingMode;

    @ApiModelProperty(value = "接收账号")
    private String receivingAccount;

    @ApiModelProperty(value = "发送次数")
    private Integer num;

    @ApiModelProperty(value = "初始时间")
    private LocalDateTime initialTime;

    @ApiModelProperty(value = "发送时间类型(1:立即,2:延后)")
    private Integer sendTimeType;

    @ApiModelProperty(value = "延迟时间")
    private Integer delayTime;

    @ApiModelProperty(value = "时间单位")
    private String timeUnit;

    @ApiModelProperty(value = "发送时间")
    private LocalDateTime sendTime;

    @ApiModelProperty(value = "发送状态(1:发送成功,2:发送失败,3:待发送)")
    private Integer status;

    @ApiModelProperty(value = "错误信息")
    private String errMsg;

    @ApiModelProperty(value = "消息渠道")
    private Integer msgChannelType;

    @ApiModelProperty(value = "发送模板内容")
    private String templateContent;

    @ApiModelProperty(value = "sql查询语句")
    private String sqlSelect;

    @ApiModelProperty(value = "模板标题")
    private String templateTitle;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "操作状态(1:未读,2:已读,3:删除)")
    private Integer optStatus;

    @ApiModelProperty(value = "业务模块")
    private String subType;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
