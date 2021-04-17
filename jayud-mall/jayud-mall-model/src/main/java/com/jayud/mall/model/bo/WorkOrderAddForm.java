package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.jayud.mall.model.vo.TemplateUrlVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class WorkOrderAddForm {

    @ApiModelProperty(value = "主键id(工单id)", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "工单业务类型(1订单工单 2提单工单)", position = 2)
    @JSONField(ordinal = 2)
    @NotNull(message = "工单业务类型不能为空")
    private Integer businessType;

    @ApiModelProperty(value = "业务编号(订单号order_info order_no, 提单号ocean_bill order_id)", position = 3)
    @JSONField(ordinal = 3)
    private String businessNo;

    @ApiModelProperty(value = "业务id(订单id order_info id, 提单id ocean_bill id)", position = 4)
    @JSONField(ordinal = 4)
    private Long businessId;

    @ApiModelProperty(value = "优先级(1重要 2普通)", position = 5)
    @JSONField(ordinal = 5)
    @NotNull(message = "优先级不能为空")
    private Integer priority;

    @ApiModelProperty(value = "问题描述", position = 6)
    @JSONField(ordinal = 6)
    @NotNull(message = "问题描述")
    private String problemDescription;

    @ApiModelProperty(value = "文件附件(url)文件上传", position = 7)
    @JSONField(ordinal = 7)
    private List<TemplateUrlVO> fileUrls;


}
