package com.jayud.customs.model.po;

import com.alibaba.fastjson.annotation.JSONField;
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
 * 云报关到金蝶推送日志
 * </p>
 *
 * @author fachang.mao
 * @since 2020-12-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="YunbaoguanKingdeePushLog对象", description="云报关到金蝶推送日志")
public class YunbaoguanKingdeePushLog extends Model<YunbaoguanKingdeePushLog> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "报关单号(18位)(主键)", position = 1)
    @TableId(type = IdType.INPUT)//主键手动进行赋值设置
    @JSONField(ordinal = 1)
    private String applyNo;

    @ApiModelProperty(value = "推送状态code(枚举)", position = 2)
    @JSONField(ordinal = 2)
    private String pushStatusCode;

    @ApiModelProperty(value = "推送状态msg(枚举)", position = 3)
    @JSONField(ordinal = 3)
    private String pushStatusMsg;

    @ApiModelProperty(value = "访问方IP", position = 4)
    @JSONField(ordinal = 4)
    private String ipAddress;

    @ApiModelProperty(value = "访问用户id", position = 5)
    @JSONField(ordinal = 5)
    private Integer userId;

    @ApiModelProperty(value = "创建时间", position = 6)
    @JSONField(ordinal = 6)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间", position = 7)
    @JSONField(ordinal = 7)
    private LocalDateTime updateTime;


    @Override
    protected Serializable pkVal() {
        return this.applyNo;
    }

}
