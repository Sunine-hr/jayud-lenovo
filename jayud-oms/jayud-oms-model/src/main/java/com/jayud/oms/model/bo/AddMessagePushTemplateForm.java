package com.jayud.oms.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.jayud.common.exception.JayudBizException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
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
public class AddMessagePushTemplateForm extends Model<AddMessagePushTemplateForm> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "消息编号(规则MT+4位数序列号)")
//    @NotEmpty(message = "消息编号必填")
    private String num;

    @ApiModelProperty(value = "消息名称")
    @NotEmpty(message = "消息名称必填")
    private String msgName;

    @ApiModelProperty(value = "消息类型(1:操作状态,2:客户状态)")
    @NotNull(message = "消息类型必填")
    private Integer type;

    @ApiModelProperty(value = "业务模块")
    @NotEmpty(message = "业务模块必填")
    private String subType;

    @ApiModelProperty(value = "触发状态")
    @NotEmpty(message = "触发状态必填")
    private String triggerStatus;

    @ApiModelProperty(value = "发送时间类型(1:立即,2:延后)")
    @NotNull(message = "发送时间类型必填")
    private Integer sendTimeType;

    @ApiModelProperty(value = "延迟天数")
    private Integer sendTime;

    @ApiModelProperty(value = "时间单位(1:天,2:时,3:分)")
    private Integer timeUnit;

    @ApiModelProperty(value = "发送模板内容")
    @NotEmpty(message = "发送模板内容必填")
    private String templateContent;

    @ApiModelProperty(value = "发送内容")
    private String content;

    @ApiModelProperty(value = "替换参数(多个参数使用逗号隔开)")
    @NotEmpty(message = "替换参数必填")
    private String repParam;

    @ApiModelProperty(value = "sql查询语句")
    @NotEmpty(message = "sql查询语句必填")
    private String sqlSelect;

    @ApiModelProperty(value = "模板标题")
    @NotEmpty(message = "模板标题必填")
    private String templateTitle;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "接收岗位集合")
    private List<String> posts;

    @ApiModelProperty(value = "接收岗位")
    private String post;

    public void setPosts(List<String> posts) {
        this.posts = posts;
        StringBuilder sb = new StringBuilder();
        posts.forEach(e -> sb.append(e).append(","));
        this.post = sb.toString();
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public void checkSql() {
        if (sqlSelect.contains("update")||sqlSelect.contains("delete")){
            throw new JayudBizException("请输入正确sql语句");
        }
    }
}
