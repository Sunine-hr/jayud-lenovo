package com.jayud.oauth.model.po;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户消息渠道
 * </p>
 *
 * @author LDR
 * @since 2021-08-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MsgUserChannel对象", description="用户消息渠道")
public class MsgUserChannel extends Model<MsgUserChannel> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
      private Long id;

    @ApiModelProperty(value = "消息渠道类型")
    private Integer type;

    @ApiModelProperty(value = "消息渠道名称")
    private String name;

    @ApiModelProperty(value = "渠道账号")
    private String account;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "是否选中")
    private Boolean isSelect;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
