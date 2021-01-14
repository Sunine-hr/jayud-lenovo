package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TemplateFileVO {

    @ApiModelProperty(value = "自增id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "报价模板id(quotation_template id)", position = 2)
    @JSONField(ordinal = 2)
    private Integer qie;

    @ApiModelProperty(value = "文件标题(quoted_file file_name)", position = 3)
    @JSONField(ordinal = 3)
    private String fileName;

    @ApiModelProperty(value = "是否必要(0否 1是)", position = 4)
    @TableField(value = "`options`")
    @JSONField(ordinal = 4)
    private Integer options;

    @ApiModelProperty(value = "描述", position = 5)
    @JSONField(ordinal = 5)
    private String remarks;

    @ApiModelProperty(value = "报价对应的文件id(quoted_file id)", position = 6)
    @JSONField(ordinal = 6)
    private String qfId;

    /*报价对应的文件表：quoted_file*/
    @ApiModelProperty(value = "文件分组名称", position = 7)
    @JSONField(ordinal = 7)
    private String groupName;

    @ApiModelProperty(value = "类型名称 1报关服务 2清关服务", position = 8)
    @JSONField(ordinal = 8)
    private String typesName;

    @ApiModelProperty(value = "文件代码(quoted_file id_code)", position = 9)
    @JSONField(ordinal = 9)
    private String fileCode;

    @ApiModelProperty(value = "是否审核(0否 1是)", position = 10)
    @JSONField(ordinal = 10)
    private Integer isCheck;

    @ApiModelProperty(value = "模版文件地址", position = 11)
    @JSONField(ordinal = 11)
    private String templateUrl;

}
