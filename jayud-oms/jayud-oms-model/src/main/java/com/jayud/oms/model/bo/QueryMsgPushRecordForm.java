package com.jayud.oms.model.bo;

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
public class QueryMsgPushRecordForm extends BasePageForm {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "接收人名称")
    private String recipientName;

    @ApiModelProperty(value = "接收人id")
    private Long recipientId;

    @ApiModelProperty(value = "消息类型(1:操作状态,2:客户状态)")
    private Integer type;

//    @ApiModelProperty(value = "岗位")
//    private String post;

    @ApiModelProperty(value = "接收状态")
    private String receivingStatus;

    @ApiModelProperty(value = "接收方式")
    private String receivingMode;

    @ApiModelProperty(value = "发送状态(1:发送成功,2:发送失败,3:待发送)")
    private Integer status;

//    @ApiModelProperty(value = "消息渠道")
//    private Integer msgChannelType;

//    @ApiModelProperty(value = "标题")
//    private String title;

    @ApiModelProperty(value = "操作状态(1:未读,2:已读,3:删除)")
    private String optStatus;

    @ApiModelProperty(value = "业务模块")
    private String subType;
}
