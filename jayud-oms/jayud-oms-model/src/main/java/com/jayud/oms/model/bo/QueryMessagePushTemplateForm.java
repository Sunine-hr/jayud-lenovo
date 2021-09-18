package com.jayud.oms.model.bo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.jayud.common.utils.Utilities;
import com.jayud.oms.model.vo.MessagePushTemplateVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
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
public class QueryMessagePushTemplateForm extends BasePageForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "消息编号")
    private String num;

    @ApiModelProperty(value = "消息名称")
    private String msgName;

    @ApiModelProperty(value = "消息类型(1:操作状态,2:客户状态)")
    private Integer type;

    @ApiModelProperty(value = "业务模块")
    private String subType;

    @ApiModelProperty(value = "提醒状态")
    private String triggerStatus;

    @ApiModelProperty(value = "岗位集合")
    private List<String> posts;

    @ApiModelProperty(value = "状态(0禁用 1启用)")
    private Integer status;


    public static void main(String[] args) {
        System.out.println(Utilities.printFieldsInfo(AddMessagePushTemplateForm.class));
    }
}
