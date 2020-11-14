package com.jayud.mall.model.po;

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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
