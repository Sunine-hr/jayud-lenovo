package com.jayud.mall.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class TemplateFileVO {

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "报价模板id(quotation_template id)")
    private Integer qie;

    @ApiModelProperty(value = "文件标题(quoted_file file_name)")
    private String fileName;

    @ApiModelProperty(value = "是否必要(0否 1是)")
    @TableField(value = "`options`")
    private Integer options;

    @ApiModelProperty(value = "描述")
    private String remarks;

    @ApiModelProperty(value = "报价对应的文件id(quoted_file id)")
    private String qfId;

    /*报价对应的文件表：quoted_file*/
    @ApiModelProperty(value = "文件分组代码")
    private String groupCode;

    @ApiModelProperty(value = "文件分组名称")
    private String groupName;

    /*按类型分组：买单报关，独立报关*/
    private List<TemplateFileVO> templateFileVOList;

}
