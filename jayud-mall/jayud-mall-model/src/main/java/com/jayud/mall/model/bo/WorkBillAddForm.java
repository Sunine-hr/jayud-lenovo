package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.jayud.mall.model.vo.TemplateUrlVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class WorkBillAddForm {

    @ApiModelProperty(value = "主键id(工单id)", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "提单id(ocean_bill id)", position = 3)
    @JSONField(ordinal = 3)
    @NotNull(message = "提单id不能为空")
    private Long billId;

    @ApiModelProperty(value = "提单号(供应商提供)(冗余字段，减少查询)(ocean_bill order_id)", position = 4)
    @JSONField(ordinal = 4)
    private String billNo;

    @ApiModelProperty(value = "优先级(1重要 2普通)", position = 5)
    @JSONField(ordinal = 5)
    @NotNull(message = "优先级不能为空")
    private Integer priority;

    @ApiModelProperty(value = "问题描述", position = 6)
    @JSONField(ordinal = 6)
    @NotNull(message = "问题描述")
    private String problemDescription;

    @ApiModelProperty(value = "文件附件(url)文件上传", position = 15)
    @JSONField(ordinal = 15)
    private List<TemplateUrlVO> fileUrls;

}
