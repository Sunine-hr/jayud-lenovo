package com.jayud.mall.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * 报价对应的文件表
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="QuotedFile对象", description="报价对应的文件表")
public class QuotedFile extends Model<QuotedFile> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增加id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "文件分组代码")
    private String groupCode;

    @ApiModelProperty(value = "文件分组名称")
    private String groupName;

    @ApiModelProperty(value = "文件代码")
    private String idCode;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "是否必要(0否 1是)")
    @TableField(value = "`options`")
    private Integer options;

    @ApiModelProperty(value = "是否审核(0否 1是)")
    private Integer isCheck;

    @ApiModelProperty(value = "模版文件地址")
    private String templateUrl;

    @ApiModelProperty(value = "说明")
    @TableField(value = "`describe`")
    private String describe;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    @TableField(value = "`status`")
    private String status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
