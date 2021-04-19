package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class WorkOrderReplyForm {

    @ApiModelProperty(value = "主键id(工单id)", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(ordinal = 1)
    @NotNull(message = "工单id不能为空")
    private Long id;

    @ApiModelProperty(value = "问题回复(操作人回复)", position = 14)
    @JSONField(ordinal = 14)
    @NotNull(message = "回复不能为空")
    private String revert;

}
