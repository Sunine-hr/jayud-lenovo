package com.jayud.oms.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.enums.MsgTempTriggerStatusEnum;
import com.jayud.common.enums.StatusEnum;
import com.jayud.common.enums.SubOrderSignEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 消息推送模板
 * </p>
 *
 * @author LDR
 * @since 2021-08-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "MessagePushTemplate对象", description = "消息推送模板")
public class MessagePushTemplateVO extends Model<MessagePushTemplateVO> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "消息编号(规则MT+4位数序列号)")
    private String num;

    @ApiModelProperty(value = "消息名称")
    private String msgName;

    @ApiModelProperty(value = "消息类型")
    private Integer type;

    @ApiModelProperty(value = "业务模块")
    private String subType;

    @ApiModelProperty(value = "业务模块描述")
    private String subTypeDesc;

    @ApiModelProperty(value = "触发状态")
    private String triggerStatus;

    @ApiModelProperty(value = "触发状态描述")
    private String triggerStatusDesc;

    @ApiModelProperty(value = "发送时间类型(1:立即,2:延后)")
    private Integer sendTimeType;

    @ApiModelProperty(value = "发送时间")
    private String sendTimeDesc;

    @ApiModelProperty(value = "延迟天数")
    private Integer sendTime;

    @ApiModelProperty(value = "时间单位")
    private String timeUnit;

    @ApiModelProperty(value = "发送模板内容")
    private String templateContent;

    @ApiModelProperty(value = "发送内容")
    private String content;

    @ApiModelProperty(value = "替换参数(多个参数使用逗号隔开)")
    private String repParam;

    @ApiModelProperty(value = "sql查询语句")
    private String sqlSelect;

    @ApiModelProperty(value = "模板标题")
    private String templateTitle;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "接收岗位")
    private String post;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "状态(0禁用 1启用)")
    private Integer status;

    @ApiModelProperty(value = "状态")
    private String statusDesc;

    @ApiModelProperty(value = "岗位集合")
    private List<String> posts = new ArrayList<>();


    public void setSubType(String subType) {
        this.subType = subType;
        this.subTypeDesc = SubOrderSignEnum.getEnum(subType).getDesc();
    }

    public void setTriggerStatus(String triggerStatus) {
        this.triggerStatus = triggerStatus;
        this.triggerStatusDesc = MsgTempTriggerStatusEnum.getDesc(triggerStatus);
    }

    public void setSendTimeType(Integer sendTimeType) {
        this.sendTimeType = sendTimeType;
        if (sendTimeType == 1) {
            sendTimeDesc = "立即";
        }
        if (sendTimeType == 2) {
            sendTimeDesc = "延迟" + (sendTime == null ? 0 : sendTime) + timeUnit;
        }
    }

    public void setPost(String post) {
        this.post = post;
        posts.addAll(Arrays.asList(this.post.split(",")));
    }

    public void setStatus(Integer status) {
        this.status = status;
        this.statusDesc = StatusEnum.getDesc(status);
    }
}
