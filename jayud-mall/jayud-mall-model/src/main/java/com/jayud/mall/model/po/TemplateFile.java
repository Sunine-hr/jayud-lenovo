package com.jayud.mall.model.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 模板对应模块信息
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TemplateFile对象", description="模板对应模块信息")
public class TemplateFile extends Model<TemplateFile> {

    private static final long serialVersionUID = 1L;

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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
