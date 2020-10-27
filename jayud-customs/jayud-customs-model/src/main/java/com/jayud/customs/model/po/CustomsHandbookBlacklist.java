package com.jayud.customs.model.po;

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
 * 报关手册黑名单词汇表
 * </p>
 *
 * @author william.chen
 * @since 2020-10-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CustomsHandbookBlacklist对象", description="报关手册黑名单词汇表")
public class CustomsHandbookBlacklist extends Model<CustomsHandbookBlacklist> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)

    private Integer id;

    @ApiModelProperty(value = "黑名单目标次")
    private String name;

    @ApiModelProperty(value = "替换名词")
    private String replacement;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private String createUser;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
